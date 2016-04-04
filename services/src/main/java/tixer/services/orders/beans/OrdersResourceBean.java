package tixer.services.orders.beans;

import tixer.data.dao.CartItemsManager;
import tixer.data.dao.ShipmentsManager;
import tixer.data.dao.TicketsManager;
import tixer.data.pojo.CartItem;
import tixer.services.orders.OrdersResource;
import tixer.services.orders.vo.request.NewItemRequest;
import tixer.services.orders.vo.response.CartResponse;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by slawek@t01.pl on 2016-03-29.
 */
@Stateless
public class OrdersResourceBean implements OrdersResource {

    @Context
    private SecurityContext securityContext;

    @EJB
    TicketsManager tickets;

    @EJB
    CartItemsManager cartItems;


    @EJB
    ShipmentsManager shipmentMethods;

    @EJB
    ShipmentsManager shipments;

    public String new_order()
    {
        Map<String, Object> map = ( HashMap<String, Object> ) securityContext.getUserPrincipal();
        return map.get("iss").toString();
    }

    public CartItem add_item( NewItemRequest item )
    {
        Integer uid = Integer.parseInt(((HashMap<String, Object>) securityContext.getUserPrincipal()).get("iss").toString());
        switch( item.item_class.toUpperCase() )
        {
            case TicketsManager.type:
                return tickets.book(
                        item.item_id,
                        item.quantity,
                        uid
                );
            default:
                throw new WebApplicationException( "Item class '"+item.item_class+"' was not implemented", 400 );
        }
    }

    public Object make_order()
    {
        return cartItems.group(
                Integer.parseInt(((HashMap<String, Object>) securityContext.getUserPrincipal()).get("iss").toString()),
                null
        );
    }

    public Object make_order( Collection<Integer> items )
    {
        return cartItems.group(
                Integer.parseInt( (( HashMap<String, Object> ) securityContext.getUserPrincipal()).get("iss").toString() ),
                items
        );
    }

    public void remove()
    {
        Integer uid = Integer.parseInt(((HashMap<String, Object>) securityContext.getUserPrincipal()).get("iss").toString());
        cartItems.remove( uid );
    }

    public void remove( Collection<Integer> items )
    {
        Integer uid = Integer.parseInt(((HashMap<String, Object>) securityContext.getUserPrincipal()).get("iss").toString());
        cartItems.remove(uid, items);
    }

    public Object cart( )
    {
        Integer uid = Integer.parseInt(((HashMap<String, Object>) securityContext.getUserPrincipal()).get("iss").toString());

        List<CartItem> ci = cartItems.getReserved(uid);
        ci.stream().forEach(c -> c.shipments = shipments.fetch(c.weight, c.item_class, c.user_id));
        return ci;
    }

    public Object get_shipments( Collection<Integer> items )
    {
        //return cartItems.getWeights( items );
        return null;
    }
}
