package tixer.data.dao;

import org.mindrot.jbcrypt.BCrypt;
import tixer.data.pojo.Admin;
import tixer.data.pojo.FBUser;
import tixer.data.pojo.User;
import tixer.system.beans.KeyFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Selection;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

/**
 * Created by slawek@t01.pl on 2016-03-25.
 */
@Stateless
public class UserManager {

    @PersistenceContext
    EntityManager em;

    @EJB
    KeyFactory key;

    public Optional<Admin> check( String email, String password )
    {
        return em.createQuery("SELECT u FROM Admin u WHERE u.email = :email", Admin.class)
                .setParameter("email", email)
                .getResultList()
                .stream()
                .filter(u -> BCrypt.checkpw(password, "$2a" + u.password.substring(3)))
                .findFirst();
    }

    public Optional<User> check( String fb_token )
    {
        Client client = ClientBuilder.newClient();
        WebTarget userTarget = client.target("https://graph.facebook.com/me");
        Response res = userTarget
                .queryParam("access_token", fb_token)
                .request("application/json").get();
        FBUser fb_user = res.readEntity(FBUser.class);

        if( fb_user.error == null ) {
            return em.createQuery("SELECT u FROM User u WHERE u.active = 1 AND u.fb_id = :fb_id", User.class)
                    .setParameter("fb_id", fb_user.id)
                    .getResultList()
                    .stream()
                    .findFirst();
        }
        else
        {
            throw new NotAuthorizedException(
                    fb_user.error.message,
                    Response.status(Response.Status.UNAUTHORIZED));
        }
    }
}
