package tixer.business.shipments.base;

import tixer.business.units.base.UnitInterface;
import tixer.business.shipments.annotation.PacksAnnotation;
import tixer.data.ddao.beans.CartItemDaoBean;
import tixer.system.helpers.UserContext;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import java.lang.annotation.Annotation;

/**
 * Created by slawek@t01.pl on 2016-04-13.
 */
public abstract class PackAbstract implements PackInterface {

    protected String name;

    @EJB
    protected CartItemDaoBean cartItemDaoBean;

    @Inject
    UserContext userContext;

    public PackAbstract() {
        Annotation annotation = getClass().getAnnotation( PacksAnnotation.class );
        if (annotation != null) {
            try {
                name = (String) annotation.annotationType().getMethod( "value" ).invoke(annotation);
            } catch (Exception ex) {
            }
        }
    }

    public void add( UnitInterface good, Integer quantity )
    {
        /** ToDo add delivery method ban check for user */

        if( !checkIfApplicable( good ) )
            throw new WebApplicationException( "Bad shipment type specified. You can't use " + name + " with " + good.getType(), 400 );

        if( !checkWeight( good ) )
            throw new WebApplicationException( "Your package is too heavy.", 400 );


    }

    public Integer getWeight( )
    {
        return cartItemDaoBean
                .getReservationsForUser(userContext.getSub())
                .stream()
                .filter(i -> i.shipment_type.equals(name))
                .map(i -> i.weight * i.quantity)
                .reduce(0, (x, y) -> x + y);
    }

    public boolean checkWeight( UnitInterface good )
    {
        return getWeight() + good.getWeight() <= getMaxWeight();
    }

    public boolean isNotEmpty()
    {
        return cartItemDaoBean
                .getReservationsForUser(userContext.getSub())
                .stream()
                .filter(i -> i.shipment_type.equals(name))
                .count() == 0;
    }
}
