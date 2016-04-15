package tixer.business.shipments.base;

import tixer.business.goods.base.GoodInterface;

/**
 * Created by slawek@t01.pl on 2016-04-13.
 */
public interface PackInterface {

    void add(Integer user_id, GoodInterface good, Integer quantity);

    boolean checkIfApplicable(GoodInterface good);

    boolean checkWeight(Integer user_id, GoodInterface good);

    Integer getWeight(Integer user_id);

    Integer getMaxWeight(Integer user_id);

    boolean isNotEmpty(Integer user_id);
}
