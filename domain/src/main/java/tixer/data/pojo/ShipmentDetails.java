package tixer.data.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by slawek@t01.pl on 2016-04-01.
 */
@Entity
@Table(name = "api_shipment_details")
public class ShipmentDetails {

    @Id
    @GeneratedValue
    public Integer id;

    public String name;
    public String address;
    public String postal_code;
    public String city;
    public String country;
    public String phone;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn( name="shipment_id", columnDefinition = "int(10) unsigned" )
    public Shipment shipment;

    public Date created_at;
    public Date packed_at;
    public Date sent_at;
}
