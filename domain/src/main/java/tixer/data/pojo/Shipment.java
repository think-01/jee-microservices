package tixer.data.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;

/**
 * Created by slawek@t01.pl on 2016-04-01.
 */
@Entity
@Table(name = "shipment")
public class Shipment {

    @Id
    @GeneratedValue
    public Integer id;

    public String name;

    public Double price;

    public Double vat;

    public Integer days;

    public Integer addy;
    public Integer phone;

    public Integer weight;
}
