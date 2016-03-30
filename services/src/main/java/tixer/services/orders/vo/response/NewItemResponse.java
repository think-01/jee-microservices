package tixer.services.orders.vo.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by slawek@t01.pl on 2016-03-29.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class NewItemResponse {

    public Integer item_id;

    public NewItemResponse(Integer item_id) {
        this.item_id = item_id;
    }
}
