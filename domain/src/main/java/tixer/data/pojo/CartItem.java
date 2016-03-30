package tixer.data.pojo;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by slawek@t01.pl on 2016-03-29.
 */
@Entity
@Table(name = "api_cart_items")
public class CartItem {

    @Id
    @GeneratedValue
    public Integer id;

    @Column( nullable = false )
    public Integer item_id;

    @Column( nullable = false )
    public String item_class;

    @Column( nullable = false )
    public Integer quantity;

    public Double price;

    @Column( nullable = false )
    public Date created_at;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn( name="order_id" )
    public Order order;

    @Column( nullable = false )
    public Integer user_id;

    public int deleted;
}
