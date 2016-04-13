package tixer.data.goodies.base;

import tixer.data.enums.ShipmentType;
import tixer.data.pojo.CartItem;

/**
 * Created by slawek@t01.pl on 2016-04-11.
 */
public interface Goods {

    public Goods setId( Integer id );
    public Goods setShipmentType( ShipmentType shipment_type );
    public Goods setQuantity( Integer quantity );

    public Double getPrice();
    public Double getVat();
    public Integer getWeight();
    public Goods setUser(Integer uid);

    public Integer getQuantity();
    public Integer getUser();
    public String getType();
    public Integer getId();
    public ShipmentType getShipmentType();

    public CartItem toCartItem();
}
