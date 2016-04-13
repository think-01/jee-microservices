package tixer.data.dao;

/**
 * Created by slawek@t01.pl on 2016-03-08.
 */
import tixer.data.pojo.Role;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Map;

@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
public class RolesManager {

    @PersistenceContext
    EntityManager em;

    private Map<Integer,String> roles = new HashMap<Integer,String>();

    @PostConstruct
    @Schedule(second="*", minute="*/10",hour="*", persistent=false)
    private void applicationStartup() {
        roles.clear();
        em.createQuery("SELECT r FROM Role r", Role.class)
                .getResultList()
                .stream()
                .forEach(r -> roles.put(r.id, r.getLabel()));
    }


    public String getRole( Integer roleID ){
        if( !roles.containsKey( roleID ) )
        {
            try {
                Role r = em.find(Role.class, roleID);
                roles.put(roleID, r.getLabel());
            } catch( Exception ignored) { }
        }
        return roles.containsKey( roleID ) ? roles.get( roleID ) : "USER";
    }

    public Map<Integer, String> getRoles() {
        return roles;
    }
}

