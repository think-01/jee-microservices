package tixer.schedulers;

import tixer.data.dao.CartItemsManager;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by slawek@t01.pl on 2016-03-29.
 */
@Singleton
public class OrdersSchedulerBean {

    @EJB
    CartItemsManager cartItems;

    @Schedule(second="*", minute="*/4",hour="*", persistent=false)
    public void clean_order_reservations(){
        cartItems.cleanup();
    }

}
