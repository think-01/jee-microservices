package tixer.services.orders.beans;

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
import tixer.business.goods.base.GoodInterface;
import tixer.business.goods.annotation.GoodsAnnotationImpl;
import tixer.data.pojo.Order;
import tixer.data.pojo.Shipment;
import tixer.services.OrdersResource;
import tixer.services.orders.vo.request.NewItemRequest;
import tixer.services.orders.vo.request.NewOrderRequest;
import tixer.system.services.JWTService;

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
public class OrdersResourceBean extends JWTService implements OrdersResource {

    @EJB
    CartItemDaoBean cartItemsDao;

    @EJB
    ShipmentDaoBean shipmentDaoBean;

    @EJB
    APIGenericDaoBean<Order> orderDaoBean;

    @Inject @Any
    Instance<GoodInterface> goods;

    @Inject @Any
    Instance<DepotInterface> depots;

    @Inject @Any
    Instance<PackInterface> packs;

    @Inject
    @PacksAnnotation(ShipmentType.DELIVERY)
    PackInterface deliveredPack;

    public CartItem add_item( NewItemRequest item )
    {
        GoodInterface good = goods
                .select(new GoodsAnnotationImpl(item.item_class))
                .get()
                .setId(item.item_id);

        PackInterface pack = packs
                .select(new PacksAnnotationImpl( item.shipment_type ))
                    .get();

        pack.add( sub, good, item.quantity );

        DepotInterface depot = depots
                .select (new DepotsAnnotationImpl( good.getDepot() ) )
                .get();

        return depot.reserve( sub, good, item.quantity );
    }

    public List<CartItem> cart( )
    {
        return cartItemsDao.getReservationsForUser(sub);
    }

    public Collection<Shipment> get_shipments()
    {
        Integer cartWeight = deliveredPack.getWeight(sub);

        return shipmentDaoBean
                .all()
                .stream()
                .filter(i -> i.weight >= cartWeight )
                .collect(Collectors.toList());
    }

    public void clear()
    {
        cartItemsDao.releaseAllReservationsForUser(sub);
    }

    public void remove( Collection<Integer> items )
    {
        cartItemsDao.releaseSomeReservationsForUser(sub, items);
    }

    public Order make_order()
    {
        return make_order(null);
    }

    public Order make_order( NewOrderRequest addy )
    {
        Order order = new Order();
        order.status = OrderStatus.NEW;

        order.user_id = sub;
        order.admin_id = admin;

        if( deliveredPack.isNotEmpty(sub) )
        {
            try
            {
                Integer weight = deliveredPack.getWeight(sub);
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
