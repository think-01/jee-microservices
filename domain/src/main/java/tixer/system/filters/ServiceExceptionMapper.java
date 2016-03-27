package tixer.system.filters;

import tixer.system.vo.ErrorResponse;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by slawek@t01.pl on 2016-03-11.
 */
@Provider
public class ServiceExceptionMapper implements ExceptionMapper<Exception>
{

    public Response toResponse( Exception e )
    {
        int status;

        try
        {
            if( e.getCause() instanceof WebApplicationException ) e = ( WebApplicationException ) e.getCause();
            status = ((WebApplicationException) e).getResponse().getStatus();
        }
        catch( Exception ex )
        {
            status = 500;
        }

        return Response
                .status(status)
                .entity( new ErrorResponse( e ) )
                .build();
    }
}