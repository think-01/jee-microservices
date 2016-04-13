package tixer.data.dao;

import tixer.data.enums.OrderStatus;
import tixer.data.enums.ShipmentType;
import tixer.data.pojo.CartItem;
import tixer.data.pojo.Order;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.ws.rs.WebApplicationException;
import java.util.*;
import java.util.concurrent.TimeUnit;

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

        List<Order> l1 = em.createQuery("SELECT o FROM Order o WHERE o.created_at < :date AND o.status = :status AND o.deleted = 0", Order.class)
                .setParameter("status", OrderStatus.NEW)
                .setParameter("date", new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(24)))
                .getResultList();

        Iterator<Order> i = l1.iterator();

        while( i.hasNext() )
        {
            Order order = i.next();
            order.deleted = 1;
            em.persist( order );

            order.getItems().forEach( ci -> remove(ci) );
        }
    }

    private void remove( CartItem ci )
    {
        ci.deleted = 1;
        em.persist( ci );
    }

    public List<CartItem> getCart( Integer uid ) {
        List<CartItem> cartItems;

        cartItems = em.createQuery("SELECT i FROM CartItem i WHERE i.order IS NULL AND i.user_id = :me AND i.deleted = 0", CartItem.class)
                .setParameter("me", uid)
                .getResultList();

        if( cartItems.size() <= 0 )
        {
            throw new WebApplicationException( "No items in cart", 400 );
        }

        return cartItems;
    }

    public Integer setOrder( Integer uid, Order order ) {

        em.persist(order);
        em.createQuery("UPDATE CartItem i SET i.order = :order, i.deleted = 0 WHERE i.user_id = :me")
                .setParameter("me", uid)
                .setParameter("order", order )
                .executeUpdate();

        return order.id;
    }

    public CartItem persist( Integer user_id, String type, Integer id, Double price, Double vat, Integer quantity, Integer weight, ShipmentType shipment )
    {
        try {
            CartItem i = em.createQuery("SELECT i FROM CartItem i WHERE i.order IS NULL AND i.user_id = :me AND i.item_id = :id AND i.item_class = :c AND i.deleted = 0", CartItem.class)
                    .setParameter("id", id)
                    .setParameter("c", type)
                    .setParameter("me", user_id)
                    .setMaxResults(1)
                    .getSingleResult();

            i.quantity = /*i.quantity + */quantity;
            i.created_at = new Date();
            i.shipment_type = shipment;
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
            i.quantity = quantity;
            i.user_id = user_id;
            i.deleted = 0;
            i.shipment_type = shipment;

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

    public List<CartItem> getReserved( Integer uid, Collection<Integer> items )
    {
        return em.createQuery("SELECT i FROM CartItem i WHERE i.id IN :ids AND i.order IS NULL AND i.user_id = :me AND i.deleted = 0", CartItem.class)
                .setParameter("me", uid)
                .setParameter("ids", items)
                .getResultList();
    }
}
