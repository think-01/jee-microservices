package tixer.system.services;

import tixer.system.security.JWTPrincipal;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

/**
 * Created by slawek@t01.pl on 2016-04-14.
 */
public abstract class JWTService {

    protected Integer admin;
    protected Integer sub;
    protected Integer me;

    @Context
    private SecurityContext securityContext;

    @PostConstruct
    protected void fetch() {
        me = ( (JWTPrincipal) securityContext.getUserPrincipal() ).me;

        if( securityContext.isUserInRole("USER") ) {
            sub = ( (JWTPrincipal) securityContext.getUserPrincipal() ).me;
            admin = null;
        }
        else {
            sub = ((JWTPrincipal) securityContext.getUserPrincipal()).sub;
            admin = ( (JWTPrincipal) securityContext.getUserPrincipal() ).me;
        }
    }
}
