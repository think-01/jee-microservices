package tixer.shop.services;

import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by slawek@t01.pl on 2016-03-24.
 */
@Path("/Cart")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class Cart {

    @GET
    @Path("/ping")
    @Produces(MediaType.APPLICATION_JSON)
    public String check()
    {
        throw new WebApplicationException( "ponggg", 418 );
    }

}
