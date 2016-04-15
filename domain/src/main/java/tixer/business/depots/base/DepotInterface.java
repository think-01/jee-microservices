package tixer.business.depots.base;

import tixer.business.goods.base.GoodInterface;
import tixer.data.pojo.CartItem;

/**
 * Created by slawek@t01.pl on 2016-04-13.
 */
public interface DepotInterface {

    void cleanUp();

    CartItem reserve(Integer user_id, GoodInterface good, Integer quantity);

}
