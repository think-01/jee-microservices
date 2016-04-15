package tixer.system.persistence;

/**
 * Created by slawek@t01.pl on 2016-04-11.
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.beans.Transient;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@MappedSuperclass
public abstract class APIEntity {

    @Id
    @GeneratedValue
    public Integer id;

    @Column( nullable = false )
    @JsonIgnore
    public Date created_at;

    @JsonIgnore
    public Date updated_at;

    @JsonIgnore
    public Date deleted_at;


}
