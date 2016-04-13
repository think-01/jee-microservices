package tixer.data.goodies.base;

import tixer.data.pojo.CartItem;
import javax.ejb.Stateful;

/**
 * Created by slawek@t01.pl on 2016-04-12.
 */
@Stateful
public abstract class Goodie implements Goods {

    public CartItem toCartItem()
    {
        CartItem i = new CartItem();
        i.item_id = getId();
        i.item_class = getType();
        i.quantity = getQuantity();
        i.user_id = getUser();
        i.shipment_type = getShipmentType();
        i.weight = getWeight();
        i.price = getPrice();
        i.vat = getVat();
        return i;
    }

}
