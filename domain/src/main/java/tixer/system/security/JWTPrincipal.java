package tixer.system.security;

import com.nimbusds.jwt.JWTClaimsSet;
import tixer.data.dao.RolesManager;

import javax.ejb.EJB;
import java.security.Principal;
import java.text.ParseException;

/**
 * Created by slawek@t01.pl on 2016-04-07.
 */
public class JWTPrincipal implements Principal {

    public Integer me;
    public Integer sub;
    public String role;

    public String email;
    public String name;
    public Integer company;

    public Object data;

    @Override
    public String getName() {
        return "USER." + me.toString();
    }

    public String getRole() {
        return role;
    }
}