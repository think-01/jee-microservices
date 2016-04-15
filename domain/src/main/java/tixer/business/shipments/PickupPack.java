package tixer.business.shipments;

import tixer.business.goods.TicketItem;
import tixer.business.goods.base.GoodInterface;
import tixer.business.shipments.annotation.PacksAnnotation;
import tixer.business.shipments.base.PackAbstract;
import tixer.business.shipments.base.PackInterface;
import tixer.data.enums.ShipmentType;

import javax.ejb.Stateless;

/**
 * Created by slawek@t01.pl on 2016-04-13.
 */
@Stateless
@PacksAnnotation( ShipmentType.ON_EVENT )
public class PickupPack extends PackAbstract implements PackInterface {

    public boolean checkIfApplicable( GoodInterface good )
    {
        return !good.getType().equals(TicketItem.name);
    }

    public Integer getMaxWeight( Integer user_id )
    {
        /** ToDo set max weight */
        return 50000;
    }
}
