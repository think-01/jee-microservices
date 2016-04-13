package tixer.data.goodies.base;

import javax.enterprise.util.AnnotationLiteral;

/**
 * Created by slawek@t01.pl on 2016-04-11.
 */
public class GoodsAnnotationImpl extends AnnotationLiteral<GoodsAnnotation> implements GoodsAnnotation {
    final String expectedName;

    public GoodsAnnotationImpl(String expectedName) {
        this.expectedName = expectedName;
    }

    @Override
    public String value() {
        return expectedName;
    }

}
