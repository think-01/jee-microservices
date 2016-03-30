package tixer.data.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by slawek@t01.pl on 2016-03-29.
 */
@Entity
@Table(name = "events_schedules")
public class EventSchedule {

    @Id
    @GeneratedValue
    public Integer id;

    public int selling_seats;

    public Date begin;
    public Date end;
    public int published;

}
