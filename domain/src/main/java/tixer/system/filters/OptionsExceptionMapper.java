package tixer.system.filters;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.spi.DefaultOptionsMethodException;

/**
 * Created by slawek@t01.pl on 2016-03-18.
 */

@Provider
public class OptionsExceptionMapper implements
        ExceptionMapper<DefaultOptionsMethodException> {

    @Override
    public Response toResponse(DefaultOptionsMethodException exception) {
        return Response.ok().build();
    }
}
