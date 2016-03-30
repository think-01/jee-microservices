package tixer.data.pojo;

import javax.persistence.*;

/**
 * Created by slawek@t01.pl on 2016-03-29.
 */
@Entity
@Table(name = "api_orders")
public class Order {

    @Id
    @GeneratedValue
    public Integer id;
}
