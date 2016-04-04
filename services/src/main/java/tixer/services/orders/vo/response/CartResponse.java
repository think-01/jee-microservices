package tixer.services.orders.vo.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import tixer.data.pojo.CartItem;

import java.util.List;

/**
 * Created by slawek@t01.pl on 2016-03-29.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class CartResponse {

    public final List<CartItem> items;
    public final double price;
    public final double vat;

    public CartResponse(List<CartItem> cart) {
        this.items = cart;

        this.price = ( double ) Math.round( cart.stream().map( i -> i.price).reduce(0D, (x, y) -> x + y)*100 )/100;
        this.vat = ( double ) Math.round( cart.stream().map(i -> i.price * i.vat).reduce(0D, (x, y) -> x + y)*100 )/100;
    }
}
