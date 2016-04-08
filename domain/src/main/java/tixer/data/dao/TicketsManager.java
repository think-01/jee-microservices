package tixer.data.dao;

import tixer.data.enums.ShipmentType;
import tixer.data.pojo.CartItem;
import tixer.data.pojo.Ticket;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.WebApplicationException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by slawek@t01.pl on 2016-03-29.
 */
@Stateless
public class TicketsManager {

    @PersistenceContext
    EntityManager em;

    @EJB
    ShipmentsManager shipments;

    @EJB
    CartItemsManager items;

    public final static String type = "TICKET";

    public CartItem book( Integer id, Integer quantity, ShipmentType shipment, Integer user_id )
    {
        if( shipment == ShipmentType.ON_EVENT )
            throw new WebApplicationException( "Bad shipment type specified. You can't receive a ticket on event this ticket is issued for :)", 400 );

        Ticket ticket = em.createQuery("SELECT t FROM Ticket t WHERE t.id = :id", Ticket.class)
                .setParameter("id", id)
                .getSingleResult();

        if( ticket.schedule.selling_seats == 1 )
            throw new WebApplicationException( "Seatsio tickets not implemented", 400 );
        else
        {
            Long reserved = items.getSoldAndReserved(TicketsManager.type, id);

            if( reserved + quantity >= ticket.count )
                throw new WebApplicationException( "Unable to book " +quantity+" tickets. Only "+( ticket.count-reserved )+" left.", 400 );
            else
            {
                /** ToDo rabaty produktowe */

                return items.persist(
                        user_id,
                        TicketsManager.type,
                        id,
                        ticket.price,
                        ticket.vat,
                        quantity,
                        0,
                        shipment
                );
            }
        }
    }

}
