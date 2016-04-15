package tixer.services;

import tixer.data.pojo.CartItem;
import tixer.data.pojo.User;
import tixer.services.users.vo.UsersFindRequest;
import tixer.system.annotations.Claims;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by slawek@t01.pl on 2016-04-15.
 */
@Path("/User")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface UsersResource {

    @POST
    @Path("/find")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed( {"USER","BOW","SUPER_ADMIN"} )
    @Claims( {"sub"} )
    List<User> find( UsersFindRequest query );

}
