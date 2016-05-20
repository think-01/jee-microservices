package tixer.business.shipments.base;

import tixer.business.units.base.UnitInterface;

/**
 * Created by slawek@t01.pl on 2016-04-13.
 */
public interface PackInterface {

    void add(UnitInterface good, Integer quantity);

    boolean checkIfApplicable(UnitInterface good);

    boolean checkWeight(UnitInterface good);

    Integer getWeight();

    Integer getMaxWeight();

    boolean isNotEmpty();
}
