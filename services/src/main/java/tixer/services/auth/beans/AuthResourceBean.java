package tixer.services.auth.beans;

import tixer.data.dao.UserManager;
import tixer.services.auth.AuthResource;
import tixer.services.auth.vo.request.LoginRequest;
import tixer.services.auth.vo.response.LoginResponse;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import tixer.data.dao.RolesManager;
import tixer.system.beans.KeyFactory;

import java.util.NoSuchElementException;

@Stateless
public class AuthResourceBean implements AuthResource {

    @EJB
    RolesManager roles;

    @EJB
    UserManager user;

    @EJB
    KeyFactory key;

    @PersistenceContext
    EntityManager em;

    public String check()
    {
        throw new WebApplicationException( "pong", 418 );
    }

    public Object getRoles( ) {
        return roles.getRoles();
    }

    public LoginResponse login (
            LoginRequest login
    ) {
        try {

            if( login.fb_token == null )
            {
                return user.check( login.email, login.password )
                        .map(
                                u -> new LoginResponse(
                                        u.id.toString(),
                                        u.email,
                                        key.issue(u.id.toString()),
                                        u.role,
                                        u.company
                                )
                        )
                        .get();
            }
            else
            {
                return user.check(login.fb_token )
                        .map(
                                u -> new LoginResponse(
                                        u.id.toString(),
                                        u.email,
                                        key.issue(u.id.toString()),
                                        u.name
                                )
                        )
                        .get();
            }

        }
        catch ( NoSuchElementException e )
        {
            throw new NotAuthorizedException(
                    "Invalid login data",
                    Response.status(Response.Status.UNAUTHORIZED));
        }
        catch ( Exception e ) {
            throw new NotAuthorizedException(
                    e.getMessage(),
                    Response.status(Response.Status.UNAUTHORIZED));
        }
    }
}