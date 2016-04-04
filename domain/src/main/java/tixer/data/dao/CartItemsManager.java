package tixer.data.dao;

import tixer.data.pojo.CartItem;
import tixer.data.pojo.Order;
import tixer.data.pojo.ShipmentDetails;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.ws.rs.WebApplicationException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by slawek@t01.pl on 2016-03-29.
 */
@Stateless
public class CartItemsManager {

    @PersistenceContext
    EntityManager em;

    @EJB
    ShipmentsManager shipments;

    public void cleanup()
    {
        em.createQuery("UPDATE CartItem i SET i.deleted = 1 WHERE i.created_at < :date AND i.order IS NULL")
                .setParameter("date", new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(10)))
                .executeUpdate();
    }

    private List<CartItem> getCart( Integer uid, Collection<Integer> items ) {
        List<CartItem> cartItems;

        if( items == null )
        {
            cartItems = em.createQuery("SELECT i FROM CartItem i WHERE i.order IS NULL AND i.user_id = :me AND i.deleted = 0", CartItem.class)
                    .setParameter("me", uid)
                    .getResultList();

            if( cartItems.size() <= 0 )
            {
                throw new WebApplicationException( "No items in cart", 400 );
            }
        }
        else
        {
            cartItems = em.createQuery("SELECT i FROM CartItem i WHERE i.order IS NULL AND i.user_id = :me AND i.deleted = 0 AND id IN :ids", CartItem.class)
                    .setParameter("me", uid)
                    .setParameter("ids", items)
                    .getResultList();

            if( items.size() > cartItems.size() )
            {
                List<Integer> f = cartItems.stream().map(i -> i.id).collect(Collectors.toList());
                String s = Arrays.toString(items.stream().filter(i -> !f.contains(i)).toArray());
                throw new WebApplicationException( "Some items are invalid: " +s, 400 );
            }
        }

        return cartItems;
    }

    public Integer group( Integer uid, Collection<Integer> items ) {
        List<CartItem> cartItems = getCart(uid, items);

        if( cartItems.stream().filter(i -> i.shipment != null ).count() < cartItems.size() )
        {
            throw new WebApplicationException( "Some items do not have delivery method set.", 400 );
        }

        items = cartItems.stream().map(i -> i.id).collect(Collectors.toSet());

        Order order = new Order();
        em.persist( order );
        em.createQuery("UPDATE CartItem i SET i.order = :order, i.deleted = 0 WHERE i.user_id = :me AND id IN :ids" )
                .setParameter("me", uid)
                .setParameter("ids", items)
                .setParameter("order", order )
                .executeUpdate();

        return order.id;
    }

    public CartItem persist( Integer user_id, String type, Integer id, Double price, Double vat, Integer quantity, Integer weight )
    {
        try {
            CartItem i = em.createQuery("SELECT i FROM CartItem i WHERE i.order IS NULL AND i.user_id = :me AND i.item_id = :id AND i.item_class = :c AND i.deleted = 0", CartItem.class)
                    .setParameter("id", id)
                    .setParameter("c", type)
                    .setParameter("me", user_id)
                    .getSingleResult();

            i.quantity = /*i.quantity + */quantity;
            i.created_at = new Date();
            i.shipments = shipments.fetch( weight, type, user_id );
            em.persist(i);

            return i;
        }
        catch ( NoResultException e ) {
            CartItem i = new CartItem();
            i.created_at = new Date();
            i.item_id = id;
            i.item_class = type;

            i.price = price;
            i.vat = vat;

            i.shipments = shipments.fetch( weight, type, user_id );

            i.quantity = quantity;
            i.user_id = user_id;
            i.deleted = 0;

            i.weight = weight;
            em.persist(i);

            return i;
        }
    }

    public void remove( Integer user_id, Collection<Integer> items )
    {
        em.createQuery("UPDATE CartItem i SET i.deleted = 1 WHERE i.id IN :ids AND i.order IS NULL AND i.user_id = :me")
                .setParameter("ids", items)
                .setParameter("me", user_id)
                .executeUpdate();
    }

    public void remove( Integer user_id )
    {
        em.createQuery("UPDATE CartItem i SET i.deleted = 1 WHERE i.order IS NULL AND i.user_id = :me")
                .setParameter( "me", user_id )
                .executeUpdate();
    }

    public Long getSoldAndReserved( String type, Integer item_id )
    {
        Long reserved = em.createQuery("SELECT SUM( i.quantity ) FROM CartItem i WHERE i.item_id = :id AND i.item_class = :c AND i.deleted = 0", Long.class)
                .setParameter("id", item_id)
                .setParameter("c", type)
                .getSingleResult();

        return reserved == null ? 0L : reserved;
    }

    public Long getReserved( String type, Integer item_id, Integer uid )
    {
        Long reserved = em.createQuery("SELECT SUM( i.quantity ) FROM CartItem i WHERE i.order IS NULL AND i.user_id = :me AND i.item_id = :id AND i.item_class = :c AND i.deleted = 0", Long.class)
                .setParameter("id", item_id)
                .setParameter("c", type)
                .setParameter("me", uid)
                .getSingleResult();

        return reserved == null ? 0L : reserved;
    }

    public List<CartItem> getReserved( Integer uid )
    {
        return em.createQuery("SELECT i FROM CartItem i WHERE i.order IS NULL AND i.user_id = :me AND i.deleted = 0", CartItem.class)
                .setParameter("me", uid)
                .getResultList();
    }
}
