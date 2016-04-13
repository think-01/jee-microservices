package tixer.data.goodies;

import tixer.data.ddao.generic.OldGenericDaoBean;
import tixer.data.ddao.beans.CartItemDaoBean;
import tixer.data.enums.ShipmentType;
import tixer.data.goodies.base.Goodie;
import tixer.data.goodies.base.Goods;
import tixer.data.goodies.base.GoodsAnnotation;
import tixer.data.pojo.Ticket;
import tixer.system.helpers.Produced;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;

/**
 * Created by slawek@t01.pl on 2016-04-11.
 */
@Stateful
@GoodsAnnotation( TicketItem.name )
public class TicketItem extends Goodie implements Goods {

    public static final String name = "TICKET";

    @Inject
    @Produced
    private OldGenericDaoBean<Ticket> ticketsDao;

    @EJB
    CartItemDaoBean cartItemsDao;

    private Ticket ticket;
    private ShipmentType shipment;
    private Integer user;
    private Integer id;
    private Integer quantity;

    public TicketItem setShipmentType( ShipmentType shipment_type )
    {
        if( shipment_type == ShipmentType.ON_EVENT )
            throw new WebApplicationException( "Bad shipment type specified. You can't receive a ticket on event this ticket is issued for :)", 400 );

        shipment = shipment_type;
        return this;
    }

    public TicketItem setId( Integer id ) {
        ticket = ticketsDao.find(id);

        if (ticket.schedule.selling_seats == 1)
            throw new WebApplicationException("Seatsio tickets not implemented", 400);

        this.id = id;
        return this;
    }

    public TicketItem setQuantity( Integer quantity )
    {
        if( quantity > 0 )
        {
            Long reserved = cartItemsDao.getSoldAndReserved( user, TicketItem.name, ticket.id );

            if( reserved + quantity >= ticket.count )
                throw new WebApplicationException( "Unable to book " +quantity+" tickets. Only "+( ticket.count-reserved )+" left.", 400 );

        }

        this.quantity = quantity;
        return this;
    }

    public TicketItem setUser(Integer uid){
        user = uid;
        return this;
    }

    public Double getPrice() {
        return ticket.price;
    }

    public Double getVat() {
        return ticket.vat;
    }

    public Integer getWeight() {
        return 0;
    }

    public Integer getQuantity(){
        return quantity;
    }

    public Integer getUser(){
        return user;
    }

    public String getType(){
        return name;
    }

    public Integer getId(){
        return id;
    }

    public ShipmentType getShipmentType(){
        return shipment;
    }
}
