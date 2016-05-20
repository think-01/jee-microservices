package tixer.business.shipments;

import tixer.business.units.base.UnitInterface;
import tixer.business.shipments.annotation.PacksAnnotation;
import tixer.business.shipments.base.PackAbstract;
import tixer.business.shipments.base.PackInterface;
import tixer.data.ddao.beans.ShipmentDaoBean;
import tixer.data.enums.ShipmentType;

import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Created by slawek@t01.pl on 2016-04-13.
 */
@Stateless
@PacksAnnotation( ShipmentType.DELIVERY )
public class DeliveredPack extends PackAbstract implements PackInterface {

    @EJB
    protected ShipmentDaoBean shipmentDaoBean;

    public boolean checkIfApplicable( UnitInterface good )
    {
        return true;
    }

    public Integer getMaxWeight( )
    {
        return shipmentDaoBean
                .all()
                .stream()
                .map(i -> i.weight)
                .reduce(0, (x, y) -> Math.max(x,y) );
    }
}
