package tixer.business;

import tixer.business.shipments.annotation.PacksAnnotationImpl;
import tixer.business.shipments.base.PackInterface;
import tixer.business.units.base.UnitInterface;
import tixer.data.ddao.beans.CartItemDaoBean;
import tixer.data.pojo.CartItem;
import tixer.system.helpers.UserContext;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.Collection;
import java.util.List;

/**
 * Created by slawek@t01.pl on 2016-04-25.
 */
@Stateless
public class Bucket {

    @EJB
    CartItemDaoBean cartItemsDao;

    @Inject
    UserContext userContext;

    public CartItem add( UnitInterface unit, Integer quantity ) {


        CartItem i = new CartItem();
        i.item_id = unit.getId();
        i.item_class = unit.getType();
        i.quantity = quantity;
        i.user_id = userContext.getSub();
        i.shipment_type = unit.getShipmentType();
        i.weight = unit.getWeight();
        i.price = unit.getPrice();
        i.vat = unit.getVat();
        cartItemsDao.save( i );
        return i;
    }

    public List<CartItem> get(){
        return cartItemsDao
                .getReservationsForUser( userContext.getSub() );
    }

    public void clear()
    {
        cartItemsDao.releaseAllReservationsForUser(userContext.getSub());
    }

    public void remove( Collection<Integer> items )
    {
        cartItemsDao.releaseSomeReservationsForUser(userContext.getSub(), items);
    }


}
