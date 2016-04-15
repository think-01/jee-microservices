package tixer.data.ddao.base;

import tixer.data.pojo.Shipment;
import tixer.system.persistence.APIEntity;

import javax.ejb.Stateless;
import java.util.Date;
import java.util.List;

/**
 * Created by slawek@t01.pl on 2016-04-11.
 */

public class DefinedDaoBean<E extends APIEntity> extends AbstractDaoBean {

    public void persist(final E instance) {
        instance.created_at = new Date();
        em.persist(instance);
    }

    public E find(final long id) {
        return (E) em.find( getEntityClass(), id);
    }

    public List<E> all() {
        return em.createQuery("SELECT s FROM " + getEntityClassName() + " s WHERE deleted_at IS NULL", getEntityClass())
                .getResultList();
    }

    public void remove(final E instance) {
        instance.deleted_at = new Date();
        E merge = em.merge(instance);
        em.flush();
    }

    public E merge(final E instance) {
        instance.updated_at = new Date();
        E merge = em.merge(instance);
        em.flush();
        return merge;
    }

}
