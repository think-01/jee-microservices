package tixer.services.orders.beans;

import tixer.data.ddao.base.DefinedDaoBean;
import tixer.data.ddao.beans.CartItemDaoBean;
import tixer.data.ddao.beans.ShipmentDaoBean;
import tixer.data.ddao.generic.APIGenericDaoBean;
import tixer.data.enums.OrderStatus;
import tixer.data.enums.ShipmentType;
import tixer.data.pojo.CartItem;
import tixer.data.goodies.base.Goods;
import tixer.data.goodies.base.GoodsAnnotationImpl;
import tixer.data.pojo.Order;
import tixer.data.pojo.Shipment;
import tixer.services.OrdersResource;
import tixer.services.orders.vo.request.NewItemRequest;
import tixer.services.orders.vo.request.NewOrderRequest;
import tixer.system.security.JWTPrincipal;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by slawek@t01.pl on 2016-04-11.
 */
@Stateless
public class OrdersResourceBean implements OrdersResource {

    @Context
    private SecurityContext securityContext;

    @EJB
    CartItemDaoBean cartItemsDao;

    @EJB
    ShipmentDaoBean shipmentDaoBean;

    @EJB
    APIGenericDaoBean<Order> orderDaoBean;

    @Inject @Any
    Instance<Goods> goods;

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

        Goods goodie = goods
                .select(new GoodsAnnotationImpl( item.item_class ))
                .get()
                .setUser( sub )
                .setShipmentType(item.shipment_type)
                .setId( item.item_id )
                .setQuantity(item.quantity);

        return cartItemsDao.save( goodie.toCartItem() );
    }

    public List<CartItem> cart( )
    {
        authenticate();
        return cartItemsDao.getReservationsForUser(sub);
    }

    public Collection<Shipment> get_shipments()
    {
        authenticate();
        List<CartItem> items = cartItemsDao.getReservationsForUser(sub);

        Integer weight = items.stream().filter(i -> i.shipment_type == ShipmentType.DELIVERY).map(i -> i.weight * i.quantity).reduce(0, (x, y) -> x + y);
        return shipmentDaoBean.getPossibleMethods(sub, weight);
    }

    public void clear()
    {
        authenticate();;
        cartItemsDao.releaseAllReservationsForUser(sub);
    }

    public void remove( Collection<Integer> items )
    {
        authenticate();
        cartItemsDao.releaseSomeReservationsForUser(sub, items);
    }

    public Order make_order()
    {
        return make_order(null);
    }

    public Order make_order( NewOrderRequest addy )
    {
        authenticate();
        List<CartItem> items = cartItemsDao.getReservationsForUser(sub);

        Order order = new Order();
        order.status = OrderStatus.NEW;

        order.user_id = sub;
        order.admin_id = securityContext.isUserInRole("USER") ? null : ( (JWTPrincipal) securityContext.getUserPrincipal() ).me;

        if( items.stream().filter(i -> i.shipment_type == ShipmentType.DELIVERY).count() > 0 )
        {
            try
            {
                Integer weight = items.stream().filter(i -> i.shipment_type == ShipmentType.DELIVERY).map(i -> i.weight).reduce(0, (x, y) -> x + y);

                Shipment method = shipmentDaoBean.find(addy.method);

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

        orderDaoBean.persist( order );
        cartItemsDao.setOrderForAllUserReservations( sub, order );

        return order;
    }

}
