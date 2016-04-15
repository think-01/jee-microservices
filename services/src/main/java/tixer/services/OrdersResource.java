package tixer.services;

import tixer.data.pojo.CartItem;
import tixer.data.pojo.Order;
import tixer.data.pojo.Shipment;
import tixer.services.orders.vo.request.NewItemRequest;
import tixer.services.orders.vo.request.NewOrderRequest;
import tixer.system.annotations.Claims;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.List;

/**
 * Created by slawek@t01.pl on 2016-04-12.
 */
@Path("/Order")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface OrdersResource {

    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed( {"USER","BOW","SUPER_ADMIN"} )
    @Claims( {"sub"} )
    CartItem add_item(NewItemRequest item);

    @GET
    @Path("/cart")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed( {"USER","BOW","SUPER_ADMIN"} )
    @Claims( {"sub"} )
    List<CartItem> cart();

    @GET
    @Path("/shipments")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed( {"USER","BOW","SUPER_ADMIN"} )
    @Claims( {"sub"} )
    Collection<Shipment> get_shipments();

    @GET
    @Path("/clear")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed( {"USER","BOW","SUPER_ADMIN"} )
    @Claims( {"sub"} )
    void clear();

    @POST
    @Path("/remove")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed( {"USER","BOW","SUPER_ADMIN"} )
    @Claims( {"sub"} )
    void remove(Collection<Integer> items);

    @GET
    @Path("/group")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed( {"USER","BOW","SUPER_ADMIN"} )
    @Claims( {"sub"} )
    Order make_order();

    @POST
    @Path("/group")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed( {"USER","BOW","SUPER_ADMIN"} )
    @Claims( {"sub"} )
    Order make_order(NewOrderRequest addy);
}

