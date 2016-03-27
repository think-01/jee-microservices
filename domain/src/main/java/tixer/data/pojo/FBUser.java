package tixer.data.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * Created by slawek@t01.pl on 2016-03-15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FBUser  implements Serializable {

    public String id;

    public FBError error;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class FBError
    {
        public String message;
    }
}
