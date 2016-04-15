package tixer.business.depots;

import tixer.business.depots.annotation.DepotsAnnotation;
import tixer.business.depots.base.DepotAbstract;
import tixer.business.depots.base.DepotInterface;
import tixer.business.goods.base.GoodInterface;
import tixer.data.ddao.beans.CartItemDaoBean;
import tixer.data.pojo.CartItem;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.WebApplicationException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by slawek@t01.pl on 2016-04-13.
 */
@Stateless
@DepotsAnnotation( TicketsDepot.name )
public class TicketsDepot extends DepotAbstract implements DepotInterface {

    public static final String name = "TICKET";

    @EJB
    CartItemDaoBean cartItemsDao;

    public void cleanUp()
    {
        cartItemsDao.cleanup();
    }

    public CartItem reserve( Integer user_id, GoodInterface good, Integer quantity ) {
        if( quantity <= 0 )
            throw new WebApplicationException( "Unable to book " +quantity+" tickets. Bad number specified.", 400 );

        Long reserved = cartItemsDao.getSoldAndReserved( user_id, good.getType(), good.getId() );

        if( reserved + quantity >= good.getPool() )
            throw new WebApplicationException( "Unable to book " +quantity+" tickets. Only "+( good.getPool()-reserved )+" left.", 400 );

        CartItem i = new CartItem();
        i.item_id = good.getId();
        i.item_class = good.getType();
        i.quantity = quantity;
        i.user_id = user_id;
        i.shipment_type = good.getShipmentType();
        i.weight = good.getWeight();
        i.price = good.getPrice();
        i.vat = good.getVat();

        cartItemsDao.save( i );

        return i;
    }
}
