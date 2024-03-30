package me.dhassan.api.resources;

import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.dhassan.api.contexts.SecurityContext;
import me.dhassan.api.interceptors.Authenticated;
import me.dhassan.api.responseObjects.ErrorResponse;
import me.dhassan.domain.entity.User;
import me.dhassan.infrastructure.entity.UserEntity;
import me.dhassan.infrastructure.mapper.UserMapper;
import me.dhassan.infrastructure.usecases.UserUseCasesImpl;

import java.util.Set;
import java.util.UUID;

import static me.dhassan.infrastructure.utils.Utilities.*;

@Path("/api/users")
public class UserResource {

    @Inject
    UserUseCasesImpl userUseCases;

    @Inject
    SecurityContext securityContext;

    @Inject
    UserMapper userMapper;

    @Inject
    Validator validator;

    @GET
    @Path("/validate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response validate() {
        UUID userId = securityContext.userId;

        User user = userUseCases.findUserById(userId);
        user.password = null;

        return Response.ok().entity(user).build();
    }

    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(User user) {

        // TODO fix the validator.validate(obj) problem
        // handle validation
        Set<ConstraintViolation<UserEntity>> violations = validator.validate(userMapper.mapToUserEntity(user));
        ErrorResponse errors = new ErrorResponse();
        if (!violations.isEmpty()) {
            for (ConstraintViolation<UserEntity> violation : violations) {
                errors.addError(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(errors)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }


        User userWithSameEmail = userUseCases.findUserByEmail(user.email);

        if(userWithSameEmail != null) {
            errors.addError("email", "Email is already in use");
            return Response.status(Response.Status.BAD_REQUEST).entity(errors).build();
        }


        // hash the password
        user.password = hashPassword(user.password);
        // save the new user
        User newUser = userUseCases.createUser(user);

        return Response.ok().entity(newUser).build();
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(User user) {

        // TODO fix the validator.validate(obj) problem
        // handle validation
        Set<ConstraintViolation<UserEntity>> violations = validator.validate(userMapper.mapToUserEntity(user));
        ErrorResponse errors = new ErrorResponse();
        if (!violations.isEmpty()) {
            for (ConstraintViolation<UserEntity> violation : violations) {
                errors.addError(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(errors)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        User userWithSameEmail = userUseCases.findUserByEmail(user.email);

        if(userWithSameEmail == null) {
            errors.addError("email", "Email not found");
            return Response.status(Response.Status.BAD_REQUEST).entity(errors).build();
        }


        // validate the password
        if (!compareHashedPassword(user.password, userWithSameEmail.password)) {
            errors.addError("password", "Invalid email/password combination");
            return Response.status(Response.Status.BAD_REQUEST).entity(errors).build();
        }


        String token = createToken(userWithSameEmail.id.toString());
        return Response.ok()
                .header("Authorization", token)
                .entity(token)
                .build();
    }
}
