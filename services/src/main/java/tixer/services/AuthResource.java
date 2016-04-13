package tixer.services;

import tixer.services.auth.vo.request.LoginRequest;
import tixer.services.auth.vo.response.LoginResponse;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by slawek@t01.pl on 2016-03-25.
 */
@Path("/Auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface AuthResource {

    @GET
    @Path("/ping")
    @Produces(MediaType.APPLICATION_JSON)
    public String check();

    @GET
    @RolesAllowed( {"SUPER_ADMIN"} )
    @Path("/roles")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Object getRoles( );

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public LoginResponse login (
            LoginRequest login
    );
}
