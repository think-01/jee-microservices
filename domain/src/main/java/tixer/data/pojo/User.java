package tixer.data.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;

/**
 * Created by slawek@t01.pl on 2016-03-15.
 */
@Entity
@Table(name = "clients")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class User {

    @Id
    @GeneratedValue
    public Integer id;

    public String email;

    public String password;

    public Integer active;

    @Column(name="facebook_user_id")
    public String fb_id;

    @Column(name="facebook_name")
    public String name;

    @Transient
    public String fb_token;
}
