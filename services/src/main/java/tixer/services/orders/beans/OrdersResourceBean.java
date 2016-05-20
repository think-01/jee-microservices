package tixer.services.orders.beans;

import tixer.business.Bucket;
import tixer.business.depots.annotation.DepotsAnnotationImpl;
import tixer.business.depots.base.DepotInterface;
import tixer.business.shipments.annotation.PacksAnnotation;
import tixer.business.shipments.annotation.PacksAnnotationImpl;
import tixer.business.shipments.base.PackInterface;
import tixer.data.ddao.beans.CartItemDaoBean;
import tixer.data.ddao.beans.ShipmentDaoBean;
import tixer.data.ddao.generic.APIGenericDaoBean;
import tixer.data.enums.OrderStatus;
import tixer.data.enums.ShipmentType;
import tixer.data.pojo.CartItem;
import tixer.business.units.base.UnitInterface;
import tixer.data.pojo.Order;
import tixer.data.pojo.Shipment;
import tixer.services.OrdersResource;
import tixer.services.orders.vo.request.NewItemRequest;
import tixer.services.orders.vo.request.NewOrderRequest;
import tixer.system.helpers.UserContext;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by slawek@t01.pl on 2016-04-11.
 */
@Stateless
public class OrdersResourceBean implements OrdersResource {

    @Inject
    UserContext userContext;

    @EJB
    CartItemDaoBean cartItemsDao;

    @EJB
    ShipmentDaoBean shipmentDaoBean;

    @EJB
    APIGenericDaoBean<Order> orderDaoBean;

    @Inject
    @PacksAnnotation(ShipmentType.DELIVERY)
    PackInterface deliveredPack;

    @Inject
    @Any
    Instance<DepotInterface> depots;

    @Inject
    @Any
    Instance<PackInterface> packs;

    @EJB
    Bucket bucket;

    public CartItem add_item( NewItemRequest item )
    {
        Integer i = userContext.getAdmin();

        UnitInterface unit = depots
                .select(new DepotsAnnotationImpl(item.item_class))
                .get()
                .take( item.item_id, item.quantity);

        packs
                .select(new PacksAnnotationImpl(item.shipment_type))
                .get()
                .add( unit, item.quantity);

        return bucket
                .add( unit, item.quantity );
    }

    public List<CartItem> cart( )
    {
        return bucket.get();
    }

    public Collection<Shipment> get_shipments()
    {
        Integer cartWeight = deliveredPack.getWeight();

        return shipmentDaoBean
                .all()
                .stream()
                .filter(i -> i.weight >= cartWeight )
                .collect(Collectors.toList());
    }

    public void clear()
    {
        bucket.clear();
    }

    public void remove( Collection<Integer> items )
    {
        bucket.remove(items);
    }

    public Order make_order()
    {
        return make_order(null);
    }

    public Order make_order( NewOrderRequest addy )
    {
        Order order = new Order();
        order.status = OrderStatus.NEW;

        order.user_id = userContext.getSub();
        order.admin_id = userContext.getAdmin();

        if( deliveredPack.isNotEmpty() )
        {
            try
            {
                Integer weight = deliveredPack.getWeight();
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
        cartItemsDao.setOrderForAllUserReservations( userContext.getSub(), order );

        return order;
    }

}
