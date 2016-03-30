package tixer.services.orders.vo.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by slawek@t01.pl on 2016-03-29.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class NewItemRequest {

    public Integer item_id;

    public String item_class;

    public Integer quantity;

}
