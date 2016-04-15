package tixer.data.ddao.beans;

import tixer.data.ddao.generic.OldGenericDaoBean;
import tixer.data.pojo.Shipment;

import javax.ejb.Stateless;
import java.util.Collection;

/**
 * Created by slawek@t01.pl on 2016-04-12.
 */
@Stateless
public class ShipmentDaoBean extends OldGenericDaoBean<Shipment> {
    private Object maxWeight;
    private Integer maxPossibleWeight;

    public Collection<Shipment> getPossibleMethods(Integer sub, Integer weight) {
        return em.createQuery("SELECT s FROM Shipment s WHERE deleted_at IS NULL AND s.weight >= :weight", Shipment.class)
                .setParameter("weight", weight )
                .getResultList();
    }
}
