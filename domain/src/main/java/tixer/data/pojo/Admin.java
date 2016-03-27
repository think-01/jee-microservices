package tixer.data.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by slawek@t01.pl on 2016-03-10.
 */
@Entity
@Table(name = "users")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Admin implements Serializable {

    @Id
    @GeneratedValue
    public Integer id;

    public String email;

    public String password;

    @Column(name="acl_role_id")
    public Integer role;

    @Column(name="company_id")
    public Integer company;
}
