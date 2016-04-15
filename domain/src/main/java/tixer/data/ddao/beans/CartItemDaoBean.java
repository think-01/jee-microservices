package tixer.data.ddao.beans;

import tixer.data.ddao.base.DefinedDaoBean;
import tixer.data.pojo.CartItem;
import tixer.data.pojo.Order;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by slawek@t01.pl on 2016-04-11.
 */
@Stateless
public class CartItemDaoBean extends DefinedDaoBean<CartItem> {

    public void cleanup()
    {
        em.createQuery("UPDATE CartItem i SET i.deleted_at = :date WHERE i.created_at < :date AND i.order IS NULL")
                .setParameter("date", new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(10)))
                .executeUpdate();
    }

    public Long getSoldAndReserved( Integer uid, String type, Integer item_id) {
        Long reserved = em.createQuery("SELECT SUM( i.quantity ) FROM CartItem i WHERE i.item_id = :id AND i.item_class = :c AND i.deleted_at IS NULL AND ( i.order IS NOT NULL OR i.user_id != :me )", Long.class)
                .setParameter("id", item_id)
                .setParameter("c", type)
                .setParameter("me",uid)
                .getSingleResult();

        return reserved == null ? 0L : reserved;
    }

    public CartItem save(CartItem i) {
        try
        {
            CartItem q = em.createQuery("SELECT i FROM CartItem i WHERE i.order IS NULL AND i.user_id = :me AND i.item_id = :id AND i.item_class = :c AND i.deleted = 0", CartItem.class)
                    .setParameter("id", i.item_id)
                    .setParameter("c", i.item_class)
                    .setParameter("me", i.user_id)
                    .setMaxResults(1)
                    .getSingleResult();

            q.quantity = i.quantity;
            q.shipment_type = i.shipment_type;
            merge(q);
            return q;
        }
        catch( NoResultException e )
        {
            persist(i);
            return i;
        }
    }

    public List<CartItem> getReservationsForUser(Integer uid)
    {
        return em.createQuery("SELECT i FROM CartItem i WHERE i.order IS NULL AND i.user_id = :me AND i.deleted_at IS NULL", CartItem.class)
                .setParameter("me", uid)
                .getResultList();
    }

    public void releaseAllReservationsForUser(Integer user_id) {
        em.createQuery("UPDATE CartItem i SET i.deleted_at = :now WHERE i.order IS NULL AND i.user_id = :me")
                .setParameter( "me", user_id )
                .setParameter( "now", new Date() )
                .executeUpdate();
    }

    public void releaseSomeReservationsForUser(Integer user_id, Collection<Integer> items) {
        em.createQuery("UPDATE CartItem i SET i.deleted_at = :now WHERE i.id IN :ids AND i.order IS NULL AND i.user_id = :me")
                .setParameter("ids", items)
                .setParameter("me", user_id)
                .setParameter( "now", new Date() )
                .executeUpdate();
    }

    public void setOrderForAllUserReservations(Integer user_id, Order order) {
        em.createQuery("UPDATE CartItem i SET i.order = :order, i.deleted = 0 WHERE i.user_id = :me")
                .setParameter("me", user_id)
                .setParameter("order", order )
                .executeUpdate();
    }
}
