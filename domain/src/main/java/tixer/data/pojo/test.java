package tixer.data.pojo;

import tixer.system.persistence.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by slawek@t01.pl on 2016-04-07.
 */
@Entity
@Table(name = "api_test")
public class test extends BaseEntity {


    public String test;
}
