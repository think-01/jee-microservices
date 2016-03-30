package tixer.data.dao;

import tixer.data.pojo.CartItem;
import tixer.data.pojo.Order;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.WebApplicationException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by slawek@t01.pl on 2016-03-29.
 */
@Stateless
public class CartItemsManager {

    @PersistenceContext
    EntityManager em;

    public void cleanup()
    {
        em.createQuery("UPDATE CartItem i SET i.deleted = 1 WHERE i.created_at < :date AND i.order IS NULL")
                .setParameter("date", new Date( System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(10) ) )
                .executeUpdate();
    }

    public Object group(Collection<Integer> items, Integer uid) {
        List<CartItem> cartItems = em.createQuery("SELECT i FROM CartItem i WHERE i.order IS NULL AND i.user_id = :me AND i.deleted = 0 AND id IN :ids", CartItem.class)
                .setParameter("me", uid)
                .setParameter("ids", items)
                .getResultList();

        if( items.size() > cartItems.size() )
        {
            List<Integer> f = cartItems.stream().map(i -> i.id).collect(Collectors.toList());
            String s = Arrays.toString( items.stream().filter(i -> !f.contains(i)).toArray() );
            throw new WebApplicationException( "Some items are invalid: " +s, 400 );
        }
        else
        {
            Order order = new Order();
            em.persist( order );
            em.createQuery("UPDATE CartItem i SET i.order = :order, i.deleted = 0 WHERE i.user_id = :me AND id IN :ids" )
                    .setParameter("me", uid)
                    .setParameter("ids", items)
                    .setParameter("order", order )
                    .executeUpdate();

            return order;
        }
    }
}
