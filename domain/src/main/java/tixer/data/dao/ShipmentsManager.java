package tixer.data.dao;

import tixer.data.pojo.Shipment;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Created by slawek@t01.pl on 2016-04-01.
 */
@Stateless
public class ShipmentsManager {

    @PersistenceContext
    EntityManager em;

    public List<Shipment> fetch( Integer weight, String type, Integer uid ) {

        /** ToDo
         * filtr metod dostawy dla użytkownika
         * filtr metod dla klasy artykułu
         */

        return em.createQuery("SELECT s FROM Shipment s WHERE weight >= :weight AND deleted_at IS NULL", Shipment.class)
                .setParameter("weight", weight)
                .getResultList();
    }

}
