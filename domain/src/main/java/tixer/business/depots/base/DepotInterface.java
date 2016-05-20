package tixer.business.depots.base;

import tixer.business.units.base.UnitInterface;
import tixer.data.pojo.CartItem;

/**
 * Created by slawek@t01.pl on 2016-04-13.
 */
public interface DepotInterface {

    void cleanUp();

    void checkStock(Integer user_id, UnitInterface good, Integer quantity);

    UnitInterface take( Integer item_id, Integer quantity );
}
