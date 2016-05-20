package tixer.services.users.beans;

import tixer.data.ddao.generic.OldGenericDaoBean;
import tixer.data.pojo.User;
import tixer.services.UsersResource;
import tixer.services.users.vo.UsersFindRequest;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

/**
 * Created by slawek@t01.pl on 2016-04-15.
 */
@Stateless
public class UsersResourceBean implements UsersResource {

    @EJB
    OldGenericDaoBean<User> userDaoBean;

    public List<User> find( UsersFindRequest query )
    {
        return null;
    }

}
