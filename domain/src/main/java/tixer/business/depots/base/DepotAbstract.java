package tixer.business.depots.base;

import tixer.business.depots.annotation.DepotsAnnotation;
import tixer.business.units.annotation.UnitsAnnotationImpl;
import tixer.business.units.base.UnitInterface;
import tixer.system.helpers.UserContext;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.lang.annotation.Annotation;

/**
 * Created by slawek@t01.pl on 2016-04-13.
 */
public abstract class DepotAbstract implements DepotInterface {

    private String name;

    @Inject
    @Any
    Instance<UnitInterface> units;

    @Inject
    UserContext userContext;

    public DepotAbstract() {
        Annotation annotation = getClass().getAnnotation(DepotsAnnotation.class);
        if (annotation != null) {
            try {
                name = (String) annotation.annotationType().getMethod( "value" ).invoke(annotation);
            } catch (Exception ex) {
            }
        }
    }

    public UnitInterface take( Integer item_id, Integer quantity )
    {
        UnitInterface unit = units
                .select(new UnitsAnnotationImpl(name))
                .get()
                .setId(item_id);

        checkStock(userContext.getSub(), unit, quantity);
        return unit;
    }
}
