package tixer.system.helpers;

import tixer.data.ddao.generic.OldGenericDaoBean;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 * Created by slawek@t01.pl on 2016-04-12.
 */
@Stateless
public class Producers implements Serializable {

    private static final long serialVersionUID = 1L;

    @Produces
    @Dependent
    @Produced
    public <T> OldGenericDaoBean<T> produce(InjectionPoint ip, BeanManager bm) {
        if (ip.getAnnotated().isAnnotationPresent(Produced.class)) {
            OldGenericDaoBean<T> crudDao = (OldGenericDaoBean<T>)  this.getBeanByName("genericDaoBean", bm);
            ParameterizedType type = (ParameterizedType) ip.getType();
            Type[] typeArgs = type.getActualTypeArguments();
            Class<T> entityClass = (Class<T>) typeArgs[0];
            crudDao.setEntityClass(entityClass);
            return crudDao;
        }
        throw new IllegalArgumentException("Annotation @Produced is required when injecting OldGenericDaoBean");
    }

    public Object getBeanByName(String name, BeanManager bm) { // eg. name=availableCountryDao{
        Bean bean = bm.getBeans(name).iterator().next();
        CreationalContext ctx = bm.createCreationalContext(bean); // could be inlined below
        Object o = bm.getReference(bean, bean.getBeanClass(), ctx); // could be inlined with return
        return o;
    }
}