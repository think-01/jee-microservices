package tixer.services.orders.beans;

import tixer.data.dao.CartItemsManager;
import tixer.data.dao.RolesManager;
import tixer.data.dao.ShipmentsManager;
import tixer.data.dao.TicketsManager;
import tixer.data.enums.OrderStatus;
import tixer.data.enums.ShipmentType;
import tixer.data.pojo.CartItem;
import tixer.data.pojo.Order;
import tixer.data.pojo.Shipment;
import tixer.services.orders.OrdersResource;
import tixer.services.orders.vo.request.NewItemRequest;
import tixer.services.orders.vo.request.NewOrderRequest;
import tixer.system.security.JWTPrincipal;
import tixer.system.security.JWTSecurityContext;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.validation.Valid;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.*;
import java.util.stream.Stream;

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

    private Integer me;
    private Integer sub;

    private void authenticate()
    {
        me = ( (JWTPrincipal) securityContext.getUserPrincipal() ).me;
        sub = ( (JWTPrincipal) securityContext.getUserPrincipal() ).sub;

        if( securityContext.isUserInRole("USER") ) sub = me;
        else
        {
            if( sub == 0 )
                throw new WebApplicationException( "Admins can't place orders for themselves. 'sub' is missing in JWT header claim", 400 );
        }
    }

    public CartItem add_item( NewItemRequest item )
    {
        if( item.shipment_type == null )
            throw new WebApplicationException( "Unknown shipment type specified: '"+item.getShipment()+"' known types are: "+ Arrays.stream(ShipmentType.values()).map(i -> i.name()).reduce("", (x, y) -> x + "," + y), 400 );

        authenticate();
        switch( item.item_class.toUpperCase() )
        {
            case TicketsManager.type:
                return tickets.book(
                        item.item_id,
                        item.quantity,
                        item.shipment_type,
                        sub
                );
            default:
                throw new WebApplicationException( "Item class '"+item.item_class+"' was not implemented", 400 );
        }
    }

    public Order make_order()
    {
        return make_order(null);
    }

    public Order make_order( NewOrderRequest addy )
    {
        authenticate();
        List<CartItem> items = cartItems.getReserved(sub);

        Order order = new Order();
        order.status = OrderStatus.NEW;
        order.created_at = new Date();

        order.user_id = ( (JWTPrincipal) securityContext.getUserPrincipal() ).sub;
        order.admin_id = securityContext.isUserInRole("USER") ? null : ( (JWTPrincipal) securityContext.getUserPrincipal() ).me;

        if( items.stream().filter(i -> i.shipment_type == ShipmentType.DELIVERY).count() > 0 )
        {
            try
            {
                Integer weight = items.stream().filter(i -> i.shipment_type == ShipmentType.DELIVERY).map(i -> i.weight).reduce(0, (x, y) -> x + y);

                Shipment method = shipmentMethods.getMethod(addy.method);
                if( weight > method.weight )
                    throw new WebApplicationException( "Your package is to heavy for '"+method.name+"'", 400 );

                if( method.addy > 0 )
                {
                    order.name = addy.name;
                    order.address = addy.address;
                    order.city = addy.city;
                    order.country = addy.country;
                }
                if( method.phone > 0 )
                    order.phone = addy.phone;

                order.shipment = method;
            }
            catch( WebApplicationException we )
            {
                throw we;
            }
            catch( Exception e )
            {
                throw new WebApplicationException( "You have to provide a valid shipment address.", 400 );
            }
        }

        cartItems.setOrder(sub, order);
        return order;
    }

    public void clear()
    {
        authenticate();;
        cartItems.remove(sub);
    }

    public void remove( Collection<Integer> items )
    {
        authenticate();
        cartItems.remove(sub, items);
    }

    public Object cart( )
    {
        authenticate();
        List<CartItem> ci = cartItems.getReserved(sub);
        return ci;
    }

    public Collection<Shipment> get_shipments()
    {
        authenticate();
        List<CartItem> items = cartItems.getReserved(sub);

        Integer weight = items.stream().filter(i -> i.shipment_type == ShipmentType.DELIVERY).map(i -> i.weight).reduce(0, (x, y) -> x + y);
        return shipmentMethods.getMethods( sub, weight );
    }

}
