package tixer.system.filters;

import org.apache.commons.io.IOUtils;
import tixer.system.vo.ErrorResponse;
import tixer.data.dao.LogsManager;

import javax.ejb.EJB;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.util.Date;
import java.util.UUID;

/**
 * Created by slawek@t01.pl on 2016-03-12.
 */
@Provider
public class JWTResponseFilter implements ContainerResponseFilter {

    @EJB
    LogsManager logs;

    private static final String ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";
    private static final String ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";

    private static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    private static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
    private static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";

    private static final String ACCESS_CONTROL_ALLOW_ORIGIN_ANYONE = "*";

    @Context
    HttpHeaders httpHeaders;

    @Override
    public void filter( ContainerRequestContext requestCtx, ContainerResponseContext responseCtx ) throws IOException {

        int s = responseCtx.getStatus();

        if( ( s < 200 || s >= 300 ) && s != 418 ) {

            ErrorResponse ent;
            Object _ent = responseCtx.getEntity();
            if( _ent instanceof ErrorResponse )
                ent = ( ErrorResponse ) _ent;
            else {
                ent = new ErrorResponse(_ent.toString());
                responseCtx.setEntity( ent );
            }

            String client = requestCtx.getHeaderString("X-Client");
            if( client == null ) {
                ent.fingerprint = "Warning: no X-Client header means no error logging!";
            } else {
                client = client.toUpperCase();
                if( client.equals( "SWAGGER" ) ) {
                    ent.fingerprint = "Warning: SWAGGER requests are not logged!";
                } else {
                    String uuid = UUID.randomUUID().toString();
                    ent.fingerprint = uuid;

                    String exceptionAsString;

                    try {
                        Exception ex = ((ErrorResponse) responseCtx.getEntity()).exception;
                        StringWriter sw = new StringWriter();
                        ex.printStackTrace(new PrintWriter(sw));
                        exceptionAsString = sw.toString();
                    } catch (Exception e) {
                        exceptionAsString = "";
                    }

                    URI uri = requestCtx.getUriInfo().getAbsolutePath();

                    String body;
                    try {
                        body = IOUtils.toString(requestCtx.getEntityStream(), "UTF-8") + "\n\n";
                    } catch ( Exception e ) {
                        body = "";
                    }

                    body = body.isEmpty() ? "" : body + "\n\n";

                    String log = "" +
                            (new Date()).toString() + "\n" +
                            requestCtx.getMethod() + ": " + uri.getPath() + "\n" +
                            "Authorization: " + requestCtx.getHeaderString("Authorization") + "\n" +
                            "Content-Type: " + requestCtx.getHeaderString("Content-Type") + "\n\n" +
                            "X-Client: " + client + "\n\n" +
                            body +
                            "Response: HTTP " + responseCtx.getStatus() + "\n" +
                            exceptionAsString;


                    logs.create(
                            uuid,
                            log,
                            client
                    );
                }
            }
        }

        responseCtx.getHeaders().remove("Content-Type");
        responseCtx.getHeaders().add("Content-Type", "application/json");

        String requestHeaders = httpHeaders.getHeaderString(ACCESS_CONTROL_REQUEST_HEADERS);
        String requestMethods = httpHeaders.getHeaderString(ACCESS_CONTROL_REQUEST_METHOD);

        if (requestHeaders != null)
            responseCtx.getHeaders().add(ACCESS_CONTROL_ALLOW_HEADERS, requestHeaders);

        if (requestMethods != null)
            responseCtx.getHeaders().add(ACCESS_CONTROL_ALLOW_METHODS, requestMethods);

        // TODO: development only, too permissive
        responseCtx.getHeaders().add(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_ANYONE);

//        responseCtx.getHeaders().add( "Access-Control-Allow-Origin", "*" );
//        responseCtx.getHeaders().add( "Access-Control-Allow-Headers", "Accept, Access-Control-Allow-Headers, Authorization, Content-Type, Origin, X-Client, X-Requested-With,".toLowerCase() );
//        responseCtx.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD".toLowerCase() );
    }

}
