package tixer.system.filters;

import tixer.system.beans.KeyFactory;
import tixer.data.dao.RolesManager;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;

import javax.annotation.Priority;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by slawek@t01.pl on 2016-03-07.
 */
@Provider
@Priority(Priorities.AUTHORIZATION)
public class JWTRequestFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @EJB
    KeyFactory key;

    @EJB
    RolesManager roles;

    private Pattern tokenPattern  = Pattern.compile("^Bearer$", Pattern.CASE_INSENSITIVE);

    @Override
    public void filter( ContainerRequestContext requestContext ) throws IOException {

        String authorizationHeader = requestContext.getHeaderString( HttpHeaders.AUTHORIZATION );

        Method method = resourceInfo.getResourceMethod();
        if( method.isAnnotationPresent( RolesAllowed.class ) )
        {
            if (authorizationHeader != null) {

                String token  = null;
                String[] parts = authorizationHeader.split(" ");
                if ( parts.length == 2 && tokenPattern.matcher( parts[0] ).matches() )
                    token  = parts[1];

                if (token != null) {

                    try {
                        JWT jwt = JWTParser.parse(token);
                        if (jwt instanceof SignedJWT) {
                            SignedJWT signedJWT = (SignedJWT) jwt;

                            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

                            Date expirationTime = claims.getExpirationTime();
                            Date now = new Date();
                            Date notBeforeTime = claims.getNotBeforeTime();

                            if ( notBeforeTime != null && notBeforeTime.compareTo(now) > 0 )
                                throw new NotAuthorizedException( "too early, token not valid yet", 401 );

                            if ( expirationTime != null && expirationTime.compareTo(now) <= 0 )
                                throw new NotAuthorizedException( "too late, token expired", 401 );

                            String publicKey = key.issue( claims.getIssuer() );

                            if( signedJWT.verify( new MACVerifier(publicKey) ) )
                            {
                                RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);

                                JWTPrincipal principal = new JWTPrincipal(claims);

                                if(
                                    Arrays.stream( rolesAnnotation.value() )
                                            .filter( r -> r.equals(principal.getRole()) || r.equals("ANY") )
                                            .findFirst()
                                            .isPresent()
                                )
                                {
                                    JWTSecurityContext ctx = new JWTSecurityContext(
                                            principal,
                                            requestContext.getSecurityContext().isSecure());

                                    requestContext.setSecurityContext(ctx);
                                }
                                else
                                    throw new NotAuthorizedException( "Not in role", 403 );
                            } else
                                throw new NotAuthorizedException( "Unable to verify Bearer token", 401 );
                        } else {
                            throw new NotAuthorizedException( "Unexpected JWT type", 401 );
                        }
                    } catch ( Exception e) {
                        throw new NotAuthorizedException( "Unauthorized: " + e.getMessage(), 401 );
                    }

                } else
                    throw new NotAuthorizedException( "Unauthorized: Unable to parse Bearer token", 401 );
            } else
                throw new NotAuthorizedException( "No Authorization header", 401 );
        }

    }

    public class JWTPrincipal extends HashMap<String, Object> implements Principal {

        public JWTPrincipal( final JWTClaimsSet claim ) {
            claim.getClaims().forEach( (k,v) -> this.put(k,v) );
        }

        @Override
        public String getName() {
            return "USER." + this.get( "iss" ).toString();
        }

        public String getRole() {
            if( this.containsKey("role") )
                return roles.getRole( (int) (long) this.get( "role" ) );
            else return "USER";
        }
    }

    public static class JWTSecurityContext implements SecurityContext
    {
        private JWTPrincipal principal;
        private boolean isSecure;
        private String role;

        public JWTSecurityContext( final JWTPrincipal principal, final boolean isSecure ) {
            this.principal  = principal;
            this.isSecure   = isSecure;
            this.role = principal.getRole();
        }

        @Override
        public String getAuthenticationScheme() {
            return "JWT"; // informational
        }

        @Override
        public Principal getUserPrincipal() {
            return principal;
        }

        @Override
        public boolean isSecure() {
            return isSecure;
        }

        @Override
        public boolean isUserInRole(final String role) {
            return this.role.equals( role );
        }
    }
}

