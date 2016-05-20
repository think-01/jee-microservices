package tixer.business.units.annotation;

import javax.enterprise.util.AnnotationLiteral;

/**
 * Created by slawek@t01.pl on 2016-04-11.
 */
public class UnitsAnnotationImpl extends AnnotationLiteral<UnitsAnnotation> implements UnitsAnnotation {
    final String expectedName;

    public UnitsAnnotationImpl(String expectedName) {
        this.expectedName = expectedName;
    }

    @Override
    public String value() {
        return expectedName;
    }

}
