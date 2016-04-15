package tixer.business.depots.annotation;

import javax.enterprise.util.AnnotationLiteral;

/**
 * Created by slawek@t01.pl on 2016-04-13.
 */
public class DepotsAnnotationImpl extends AnnotationLiteral<DepotsAnnotation> implements DepotsAnnotation {
    final String expectedName;

    public DepotsAnnotationImpl(String expectedName) {
        this.expectedName = expectedName;
    }

    @Override
    public String value() {
        return expectedName;
    }

}