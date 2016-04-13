package tixer.data.ddao.generic;

import tixer.data.ddao.base.DefinedDaoBean;
import tixer.system.persistence.APIEntity;

import javax.ejb.Stateless;

/**
 * Created by slawek@t01.pl on 2016-04-12.
 */
@Stateless
public class APIGenericDaoBean<E extends APIEntity> extends DefinedDaoBean {
}
