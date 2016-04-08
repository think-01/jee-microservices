package tixer.system.security;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

/**
 * Created by slawek@t01.pl on 2016-04-07.
 */
public class JWTSecurityContext implements SecurityContext
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

    public JWTPrincipal getPrincipal() {
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