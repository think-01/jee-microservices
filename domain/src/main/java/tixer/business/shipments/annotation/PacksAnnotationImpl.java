package tixer.business.shipments.annotation;

import javax.enterprise.util.AnnotationLiteral;

/**
 * Created by slawek@t01.pl on 2016-04-13.
 */
public class PacksAnnotationImpl extends AnnotationLiteral<PacksAnnotation> implements PacksAnnotation {
    final String expectedName;

    public PacksAnnotationImpl(String expectedName) {
        this.expectedName = expectedName;
    }

    @Override
    public String value() {
        return expectedName;
    }

}