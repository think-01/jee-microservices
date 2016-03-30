package tixer.services.orders.beans;

import tixer.data.dao.CartItemsManager;
import tixer.data.dao.goodies.TicketItem;
import tixer.services.orders.OrdersResource;
import tixer.services.orders.vo.request.NewItemRequest;
import tixer.services.orders.vo.response.NewItemResponse;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by slawek@t01.pl on 2016-03-29.
 */
@Stateless
public class OrdersResourceBean implements OrdersResource {

    @Context
    private SecurityContext securityContext;

    @EJB
    TicketItem tickets;

    @EJB
    CartItemsManager cartItems;

    public String new_order()
    {
        Map<String, Object> map = ( HashMap<String, Object> ) securityContext.getUserPrincipal();
        return map.get("iss").toString();
    }

    public NewItemResponse add_item( NewItemRequest item )
    {
        switch( item.item_class.toUpperCase() )
        {
            case TicketItem.type:
                return new NewItemResponse(
                        tickets.book(
                                item.item_id,
                                item.quantity,
                                Integer.parseInt( (( HashMap<String, Object> ) securityContext.getUserPrincipal()).get("iss").toString() )
                        )
                );
            default:
                throw new WebApplicationException( "Item class '"+item.item_class+"' was not implemented", 400 );
        }
    }

    public Object make_order( Collection<Integer> items )
    {
        return cartItems.group(
                items,
                Integer.parseInt( (( HashMap<String, Object> ) securityContext.getUserPrincipal()).get("iss").toString() )
        );
    }
}
