package tixer.business.goods.base;

import tixer.data.enums.ShipmentType;
import tixer.data.pojo.CartItem;

/**
 * Created by slawek@t01.pl on 2016-04-11.
 */
public interface GoodInterface {

    GoodInterface setId(Integer id);
    GoodInterface setShipmentType(String shipment_type);

    Double getPrice();
    Double getVat();
    Integer getWeight();
    Integer getPool();
    String getType();
    Integer getId();

    String getShipmentType();
    String getDepot();
}
