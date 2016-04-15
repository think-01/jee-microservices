package tixer.services.orders.vo.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import tixer.data.enums.ShipmentType;

//import javax.validation.constraints.Pattern;

/**
 * Created by slawek@t01.pl on 2016-03-29.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class NewItemRequest {

    public Integer item_id;

    //@Pattern(regexp = "[0-9]+", message = "The id must be a valid number1111")
    public String item_class;

    public Integer quantity;

    public String shipment_type;

    public void setShipment(String shipment) {
        this.shipment_type = ShipmentType.valueOf(shipment);
    }

    public void setItem_class(String item_class) {
        this.item_class = item_class.toUpperCase();
    }
}
