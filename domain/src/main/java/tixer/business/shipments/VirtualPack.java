package tixer.business.shipments;

import tixer.business.units.TicketItem;
import tixer.business.units.base.UnitInterface;
import tixer.business.shipments.annotation.PacksAnnotation;
import tixer.business.shipments.base.PackAbstract;
import tixer.business.shipments.base.PackInterface;
import tixer.data.enums.ShipmentType;

import javax.ejb.Stateless;

/**
 * Created by slawek@t01.pl on 2016-04-13.
 */
@Stateless
@PacksAnnotation( ShipmentType.VIRTUAL )
public class VirtualPack extends PackAbstract implements PackInterface {

    public boolean checkIfApplicable( UnitInterface good )
    {
        return good.getType().equals(TicketItem.name);
    }

    public Integer getMaxWeight( )
    {
        return 0;
    }
}
