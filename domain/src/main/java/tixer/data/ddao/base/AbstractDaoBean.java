package tixer.data.ddao.base;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by slawek@t01.pl on 2016-04-11.
 */
public abstract class AbstractDaoBean<E> {

    @PersistenceContext
    protected EntityManager em;

    private Class<E> entityClass;
    private String entityClassName;

    public E entity;

    public void setEntityClass(Class e) {
        entityClass = e;
        entityClassName = entityClass.getSimpleName();
    }

    protected Class<E> getEntityClass() {
        fetchEntityClass();
        return entityClass;
    }

    protected String getEntityClassName() {
        fetchEntityClass();
        return entityClassName;
    }

    private void fetchEntityClass() {
        if (entityClass == null) {
            Type su = getClass().getGenericSuperclass();
            while (su != null) {
                if (su instanceof ParameterizedType) {
                    setEntityClass((Class<E>) ((ParameterizedType) su).getActualTypeArguments()[0]);
                    break;
                } else
                    su = ((Class<E>) su).getGenericSuperclass();
            }
        }
    }
}
