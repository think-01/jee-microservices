package tixer.business.units.base;

import tixer.data.pojo.CartItem;

/**
 * Created by slawek@t01.pl on 2016-04-11.
 */
public interface UnitInterface {

    UnitInterface setId(Integer id);
    UnitInterface setShipmentType(String shipment_type);

    Double getPrice();
    Double getVat();
    Integer getWeight();
    Integer getPool();
    String getType();
    Integer getId();

    String getShipmentType();
    String getDepot();

}
