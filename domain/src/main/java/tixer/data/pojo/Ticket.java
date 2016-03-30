package tixer.data.pojo;

import javax.persistence.*;

/**
 * Created by slawek@t01.pl on 2016-03-29.
 */
@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue
    public Integer id;

    public Integer count;

    public Double price;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn( name="events_schedules_id" )
    public EventSchedule schedule;
}
