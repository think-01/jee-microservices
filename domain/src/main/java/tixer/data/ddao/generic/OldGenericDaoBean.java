package tixer.data.ddao.generic;

import tixer.data.ddao.base.AbstractDaoBean;

import javax.ejb.Stateless;
import javax.inject.Named;
import java.util.List;

/**
 * Created by slawek@t01.pl on 2016-04-11.
 */
@Stateless
@Named("genericDaoBean")
public class OldGenericDaoBean<E> extends AbstractDaoBean{

    public void persist(final E instance) {
        em.persist(instance);
    }

    public E find(final Integer id) {
        return (E) em.find( getEntityClass(), id);
    }

    public void remove(final E instance) {
        em.remove(instance);
    }

    public List<E> all() {
        return em.createQuery("SELECT s FROM " + getEntityClassName() + " s", getEntityClass())
                .getResultList();
    }

    public E merge(final E instance) {
        E merge = em.merge(instance);
        em.flush();
        return merge;
    }

}
