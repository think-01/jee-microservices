package tixer.system.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by slawek@t01.pl on 2016-03-11.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class ErrorResponse {

    public Object data;
    public String message;
    public String stamp;
    public String fingerprint;

    @JsonIgnore
    public final Exception exception;

    @JsonIgnore
    public ErrorResponse( Exception e ) {

        try
        {
            data = e.getClass().getMethod("getData", (Class<?>[]) null).invoke(e, (Object[]) null);
        }
        catch( Exception ex )
        {
            data = null;
        }

        message = e.getMessage();

        exception = e;
        this.stamp = ( (Long) System.currentTimeMillis() ).toString();
    }

    @JsonIgnore
    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }
}

