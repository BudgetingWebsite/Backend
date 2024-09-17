package org.amoseman.budgetingbackend.application.auth;

import io.dropwizard.auth.Authorizer;
import jakarta.ws.rs.container.ContainerRequestContext;
import org.checkerframework.checker.nullness.qual.Nullable;

public class UserAuthorizer implements Authorizer<User> {
    @Override
    public boolean authorize(User user, String role, @Nullable ContainerRequestContext containerRequestContext) {
        return null != user.getRoles() && user.getRoles().contains(role);
    }
}
