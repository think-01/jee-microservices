package tixer.data.dao.goodies;

import org.mindrot.jbcrypt.BCrypt;
import tixer.data.pojo.Admin;
import tixer.data.pojo.CartItem;
import tixer.data.pojo.Ticket;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.WebApplicationException;
import java.util.Date;

/**
 * Created by slawek@t01.pl on 2016-03-29.
 */
@Stateless
public class TicketItem {

    @PersistenceContext
    EntityManager em;

    public final static String type = "TICKET";

    public Integer book( Integer id, Integer quantity, Integer user_id )
    {
        Ticket ticket = em.createQuery("SELECT t FROM Ticket t WHERE t.id = :id", Ticket.class)
                .setParameter("id", id)
                .getSingleResult();

        if( ticket.schedule.selling_seats == 1 )
            throw new WebApplicationException( "Seatsio tickets not implemented", 400 );
        else
        {
            Long reserved = em.createQuery("SELECT SUM( i.quantity ) FROM CartItem i WHERE i.item_id = :id AND i.item_class = :c AND i.deleted = 0", Long.class)
                    .setParameter("id", id)
                    .setParameter("c", TicketItem.type)
                    .getSingleResult();

            if( reserved == null ) reserved = 0L;

            if( reserved + quantity >= ticket.count )
                throw new WebApplicationException( "Unable to book " +quantity+" tickets. Only "+( ticket.count-reserved )+" left.", 400 );
            else
            {
                CartItem i = new CartItem();
                i.created_at = new Date();
                i.item_id = id;
                i.item_class = TicketItem.type;
                i.price = ticket.price;
                i.quantity = quantity;
                i.user_id = user_id;
                i.deleted = 0;
                em.persist(i);

                return i.id;
            }
        }
    }

}
