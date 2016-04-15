package tixer.business.shipments.base;

import tixer.business.goods.base.GoodInterface;
import tixer.business.shipments.annotation.PacksAnnotation;
import tixer.data.ddao.beans.CartItemDaoBean;
import tixer.data.ddao.beans.ShipmentDaoBean;
import tixer.data.pojo.Shipment;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.WebApplicationException;
import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Created by slawek@t01.pl on 2016-04-13.
 */
public abstract class PackAbstract implements PackInterface {

    protected String name;

    @EJB
    protected CartItemDaoBean cartItemDaoBean;

    public PackAbstract() {
        Annotation annotation = getClass().getAnnotation( PacksAnnotation.class );
        if (annotation != null) {
            try {
                name = (String) annotation.annotationType().getMethod( "value" ).invoke(annotation);
            } catch (Exception ex) {
            }
        }
    }

    public void add( Integer user_id, GoodInterface good, Integer quantity )
    {
        /** ToDo add delivery method ban check for user */

        if( !checkIfApplicable( good ) )
            throw new WebApplicationException( "Bad shipment type specified. You can't use " + name + " with " + good.getType(), 400 );

        if( !checkWeight( user_id, good ) )
            throw new WebApplicationException( "Your package is too heavy.", 400 );

    }

    public Integer getWeight( Integer user_id )
    {
        return cartItemDaoBean
                .getReservationsForUser(user_id)
                .stream()
                .filter(i -> i.shipment_type.equals(name))
                .map(i -> i.weight * i.quantity)
                .reduce(0, (x, y) -> x + y);
    }

    public boolean checkWeight( Integer user_id, GoodInterface good )
    {
        return getWeight(user_id) + good.getWeight() <= getMaxWeight(user_id);
    }

    public boolean isNotEmpty( Integer user_id )
    {
        return cartItemDaoBean
                .getReservationsForUser(user_id)
                .stream()
                .filter(i -> i.shipment_type.equals(name))
                .count() == 0;
    }
}
