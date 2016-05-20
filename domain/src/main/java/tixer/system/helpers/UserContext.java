package tixer.system.helpers;

import tixer.system.security.JWTPrincipal;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

/**
 * Created by slawek@t01.pl on 2016-04-16.
 */
@RequestScoped
public class UserContext {

    protected Integer admin;
    protected Integer sub;
    protected Integer me;

    public void fetch( SecurityContext securityContext ) {
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

    public Integer getAdmin() {
        return admin;
    }

    public Integer getSub() {
        return sub;
    }

    public Integer getMe() {
        return me;
    }
}
