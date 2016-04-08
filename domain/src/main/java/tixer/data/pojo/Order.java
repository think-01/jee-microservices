package tixer.data.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import tixer.data.enums.OrderStatus;
import tixer.data.enums.ShipmentType;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by slawek@t01.pl on 2016-03-29.
 */
@Entity
@Table(name = "api_orders")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Order {

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

    @Enumerated( EnumType.STRING )
    public OrderStatus status;

    @OneToMany(mappedBy="order",fetch=FetchType.LAZY)
    @JsonIgnore
    private List<CartItem> items;

    public List<CartItem> getItems() {
        return items;
    }

    @JsonIgnore
    public int deleted;

    @Column( nullable = false )
    @JsonIgnore
    public Date created_at;

    @Column( nullable = false )
    @JsonIgnore
    public Integer user_id;

    @Column( nullable = true )
    @JsonIgnore
    public Integer admin_id;
}
