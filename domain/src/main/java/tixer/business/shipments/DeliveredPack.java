package tixer.business.shipments;

import tixer.business.goods.base.GoodInterface;
import tixer.business.shipments.annotation.PacksAnnotation;
import tixer.business.shipments.base.PackAbstract;
import tixer.business.shipments.base.PackInterface;
import tixer.data.ddao.beans.CartItemDaoBean;
import tixer.data.ddao.beans.ShipmentDaoBean;
import tixer.data.enums.ShipmentType;
import tixer.data.pojo.CartItem;
import tixer.data.pojo.Shipment;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by slawek@t01.pl on 2016-04-13.
 */
@Stateless
@PacksAnnotation( ShipmentType.DELIVERY )
public class DeliveredPack extends PackAbstract implements PackInterface {

    @EJB
    protected ShipmentDaoBean shipmentDaoBean;

    public boolean checkIfApplicable( GoodInterface good )
    {
        return true;
    }

    public Integer getMaxWeight( Integer user_id )
    {
        return shipmentDaoBean
                .all()
                .stream()
                .map(i -> i.weight)
                .reduce(0, (x, y) -> Math.max(x,y) );
    }
}
