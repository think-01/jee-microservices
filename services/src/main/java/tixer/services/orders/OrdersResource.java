package tixer.services.orders;

import tixer.data.pojo.CartItem;
import tixer.services.orders.vo.request.NewItemRequest;
import tixer.services.orders.vo.response.CartResponse;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collection;

/**
 * Created by slawek@t01.pl on 2016-03-29.
 */
@Path("/Order")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface OrdersResource {

    @GET
    @Path("/new")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed( {"ANY"} )
    public String new_order();

    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed( {"ANY"} )
    public CartItem add_item( NewItemRequest item );

    @POST
    @Path("/group")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed( {"ANY"} )
    public Object make_order( Collection<Integer> items );

    @GET
    @Path("/group")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed( {"ANY"} )
    public Object make_order();

    @POST
    @Path("/remove")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed( {"ANY"} )
    public void remove( Collection<Integer> items );

    @GET
    @Path("/remove")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed( {"ANY"} )
    public void remove();

    @GET
    @Path("/cart")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed( {"ANY"} )
    public Object cart();

    @POST
    @Path("/get_shipments")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed( {"ANY"} )
    public Object get_shipments( Collection<Integer> items );
}
