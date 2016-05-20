package tixer.business.depots;

import tixer.business.depots.annotation.DepotsAnnotation;
import tixer.business.depots.base.DepotAbstract;
import tixer.business.depots.base.DepotInterface;
import tixer.business.units.base.UnitInterface;
import tixer.data.ddao.beans.CartItemDaoBean;
import tixer.data.pojo.CartItem;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.WebApplicationException;

/**
 * Created by slawek@t01.pl on 2016-04-13.
 */
@Stateless
@DepotsAnnotation( TicketsDepot.name )
public class TicketsDepot extends DepotAbstract implements DepotInterface {

    public static final String name = "TICKET";

    @EJB
    CartItemDaoBean cartItemsDao;

    public void cleanUp()
    {
        cartItemsDao.cleanup();
    }

    public void checkStock(Integer user_id, UnitInterface unit, Integer quantity) {
        if( quantity <= 0 )
            throw new WebApplicationException( "Unable to book " +quantity+" tickets. Bad number specified.", 400 );

        Long reserved = cartItemsDao.getSoldAndReserved( user_id, unit.getType(), unit.getId() );

        if( reserved + quantity >= unit.getPool() )
            throw new WebApplicationException( "Unable to book " +quantity+" tickets. Only "+( unit.getPool()-reserved )+" left.", 400 );
    }
}
