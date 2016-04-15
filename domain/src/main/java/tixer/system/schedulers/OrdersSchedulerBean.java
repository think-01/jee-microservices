package tixer.system.schedulers;

import tixer.business.depots.base.DepotInterface;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 * Created by slawek@t01.pl on 2016-03-29.
 */
@Singleton
public class OrdersSchedulerBean {

    @Inject
    @Any
    Instance<DepotInterface> depots;

    @Schedule(second="*", minute="*/4",hour="*", persistent=false)
    public void depotsCleanUp(){
        depots.forEach( x -> x.cleanUp() );
    }

}
