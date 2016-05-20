package tixer.business.units.base;

import tixer.business.shipments.annotation.PacksAnnotationImpl;
import tixer.business.shipments.base.PackInterface;
import tixer.data.ddao.beans.CartItemDaoBean;
import tixer.data.pojo.CartItem;
import tixer.system.helpers.UserContext;

import javax.ejb.EJB;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 * Created by slawek@t01.pl on 2016-04-12.
 */
public abstract class UnitAbstract implements UnitInterface {
}
