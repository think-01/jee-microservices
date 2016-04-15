package tixer.data.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import tixer.data.enums.ShipmentType;
import tixer.system.persistence.APIEntity;

import javax.persistence.*;
//import javax.validation.constraints.Pattern;
/**
 * Created by slawek@t01.pl on 2016-03-29.
 */
@Entity
@Table(name = "api_cart_items")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class CartItem extends APIEntity {

    /*
    @Id
    @GeneratedValue
    public Integer id;
    */

    @Column( nullable = false )
    public Integer item_id;

    @Column( nullable = false )
    //@Pattern(regexp = "[0-9]+", message = "The id must be a valid number")
    public String item_class;

    @Column( nullable = false )
    public Integer quantity;

    public Double price;
    public Double vat;

    /*
    @Column( nullable = false )
    @JsonIgnore
    public Date created_at;
    */

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn( name="order_id" )
    public Order order;

    @Column( nullable = false )
    public Integer user_id;

    public Integer weight;

    @JsonIgnore
    public int deleted;

    public String shipment_type;
}
