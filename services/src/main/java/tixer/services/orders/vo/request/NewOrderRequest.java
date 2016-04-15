package tixer.services.orders.vo.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Map;

/**
 * Created by slawek@t01.pl on 2016-04-04.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class NewOrderRequest {

    public Integer method;

    public String name;
    public String address;
    public String postal_code;
    public String city;
    public String country;
    public String phone;

}
