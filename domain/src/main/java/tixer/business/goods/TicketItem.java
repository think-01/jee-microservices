package tixer.business.goods;

import tixer.business.depots.TicketsDepot;
import tixer.data.ddao.generic.OldGenericDaoBean;
import tixer.data.ddao.beans.CartItemDaoBean;
import tixer.data.enums.ShipmentType;
import tixer.business.goods.base.GoodAbstract;
import tixer.business.goods.base.GoodInterface;
import tixer.business.goods.annotation.GoodsAnnotation;
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
public class TicketItem extends GoodAbstract implements GoodInterface {

    public static final String name = "TICKET";

    @Inject
    @Produced
    private OldGenericDaoBean<Ticket> ticketsDao;

    @EJB
    CartItemDaoBean cartItemsDao;

    private Ticket ticket;
    private String shipment;

    public TicketItem setShipmentType( String shipment_type )
    {
        if( shipment_type.equals(ShipmentType.ON_EVENT) )
            throw new WebApplicationException( "Bad shipment type specified. You can't receive a ticket on event this ticket is issued for :)", 400 );

        shipment = shipment_type;
        return this;
    }

    public TicketItem setId( Integer id ) {
        ticket = ticketsDao.find(id);

        if (ticket.schedule.selling_seats == 1)
            throw new WebApplicationException("Seatsio tickets not implemented", 400);

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

    public Integer getPool(){
        return ticket.count;
    }

    public String getType(){
        return name;
    }

    public Integer getId(){
        return ticket.id;
    }

    public String getShipmentType(){
        return shipment;
    }

    public String getDepot() {
        /** ToDo hook seatsio depot */
        if( ticket.schedule.selling_seats == 1 )
            throw new WebApplicationException("Seatsio tickets depot is not implemented yet", 400);
        else
            return TicketsDepot.name;
    }
}
