package tixer.data.enums;

import javax.ws.rs.WebApplicationException;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Created by slawek@t01.pl on 2016-04-06.
 */
public class ShipmentType {
    public final static String VIRTUAL = "VIRTUAL";
    public final static String DELIVERY = "DELIVERY";
    public final static String ON_EVENT = "ON_EVENT";

    public static String valueOf(String shipment) {

        try {
            return ShipmentType.class.getField(shipment.toUpperCase()).getName();
        }
        catch( Exception e ) {
            throw new WebApplicationException("Bad shipment type specified.", 400);
        }
    }
}
