package me.dhassan.api.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.dhassan.api.contexts.SecurityContext;
import me.dhassan.api.interceptors.Authenticated;
import me.dhassan.domain.entity.User;
import me.dhassan.infrastructure.service.UserServiceImpl;

import java.util.UUID;

import static me.dhassan.infrastructure.utils.Utilities.*;

@Path("/api/users")
public class UserResource {

    @Inject
    UserServiceImpl userServiceImpl;

    @Inject
    SecurityContext securityContext;

    @GET
    @Path("/validate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response validate() {
        UUID userId = securityContext.userId;

        User user = userServiceImpl.findUserById(userId);
        user.password = null;

        return Response.ok().entity(user).build();
    }

    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(User user) {

        // TODO handle validation

        User userWithSameEmail = userServiceImpl.findUserByEmail(user.email);

        if(userWithSameEmail != null)
            return Response.status(Response.Status.BAD_REQUEST).entity("Email is already in use").build();


        // hash the password
        user.password = hashPassword(user.password);

        // save the new user
        User newUser = userServiceImpl.createUser(user);

        return Response.ok().entity(newUser).build();
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(User user) {

        // TODO add validation

        User userWithSameEmail = userServiceImpl.findUserByEmail(user.email);

        if(userWithSameEmail == null)
            return Response.status(Response.Status.BAD_REQUEST).entity("Email not found").build();

        // validate the password
        if (!compareHashedPassword(user.password, userWithSameEmail.password))
            return Response.status(Response.Status.BAD_REQUEST).entity("Wrong email/password combination").build();

        String token = createToken(userWithSameEmail.id.toString());
        return Response.ok()
                .header("Authorization", token)
                .entity(token)
                .build();
    }
}
