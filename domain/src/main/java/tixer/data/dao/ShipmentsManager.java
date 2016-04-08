package tixer.data.dao;

import tixer.data.pojo.CartItem;
import tixer.data.pojo.Shipment;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by slawek@t01.pl on 2016-04-01.
 */
@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
public class ShipmentsManager {

    @PersistenceContext
    EntityManager em;

    private List<Shipment> all;


    @PostConstruct
    @Schedule(second="*", minute="*/10",hour="*", persistent=false)
    private void applicationStartup() {
        if( all != null ) all.clear();
        all = em.createQuery("SELECT s FROM Shipment s WHERE deleted_at IS NULL", Shipment.class)
                .getResultList();
    }

    public Collection<Shipment> getMethods( Integer uid, Integer weight ) {

        /** ToDo
         * filtr metod dostawy dla użytkownika
         */

        return all.stream().filter(s -> s.weight >= weight).
                collect(Collectors.toList() );
    }

    public Shipment getMethod( Integer id )
    {
        return all.stream().filter( i -> i.id == id ).findFirst().get();
    }
}
