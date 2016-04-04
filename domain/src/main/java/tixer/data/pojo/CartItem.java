package tixer.data.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by slawek@t01.pl on 2016-03-29.
 */
@Entity
@Table(name = "api_cart_items")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class CartItem {

    @Id
    @GeneratedValue
    public Integer id;

    @Column( nullable = false )
    public Integer item_id;

    @Column( nullable = false )
    @JsonIgnore
    public String item_class;

    @Column( nullable = false )
    public Integer quantity;

    public Double price;
    public Double vat;

    @Column( nullable = false )
    @JsonIgnore
    public Date created_at;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn( name="order_id" )
    @JsonIgnore
    public Order order;

    @Column( nullable = false )
    @JsonIgnore
    public Integer user_id;

    public Integer weight;

    @JsonIgnore
    public int deleted;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn( name="shipment_method_id" )
    public ShipmentDetails shipment;

    @Transient
    public List<Shipment> shipments;
}
