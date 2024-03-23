package me.dhassan.api.interceptors;

import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import me.dhassan.api.contexts.SecurityContext;


import java.util.UUID;

import static me.dhassan.infrastructure.utils.Utilities.*;

@Provider
@Authenticated
public class AuthenticationFilter implements ContainerRequestFilter {

    @Inject
    SecurityContext securityContext;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        String subject = decodeTokenAndReturnSubject(authorizationHeader == null ? "" : authorizationHeader);

        if (authorizationHeader == null || subject == null) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }

        securityContext.userId = UUID.fromString(subject);
    }
}
