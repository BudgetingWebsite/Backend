package org.amoseman.budgetingwebsitebackend.resource;

import io.dropwizard.auth.Auth;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.amoseman.budgetingwebsitebackend.application.auth.Roles;
import org.amoseman.budgetingwebsitebackend.application.auth.User;
import org.amoseman.budgetingwebsitebackend.exception.UserAlreadyExistsException;
import org.amoseman.budgetingwebsitebackend.exception.UserDoesNotExistException;
import org.amoseman.budgetingwebsitebackend.pojo.account.op.CreateAccount;
import org.amoseman.budgetingwebsitebackend.service.AccountService;

import java.util.Set;

@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource<C> {
    private final AccountService<C> accountService;

    public AccountResource(AccountService<C> accountService) {
        this.accountService = accountService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAccount(CreateAccount account) {
        try {
            accountService.addAccount(account);
            return Response.ok().build();
        }
        catch (UserAlreadyExistsException e) {
            String reason = String.format("Username %s is already in use", account.getUsername());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), reason).build();
        }
    }

    @PermitAll
    @DELETE
    @Path("/{username}")
    public Response removeAccount(@Auth User user, @PathParam("username") String username) {
        if (!user.getRoles().contains(Roles.ADMIN) && !user.getName().equals(username)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        try {
            accountService.removeAccount(username);
            return Response.ok().build();
        }
        catch (UserDoesNotExistException e) {
            String reason = String.format("User %s does not exist", username);
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), reason).build();
        }
    }

    @PermitAll
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/password")
    public Response changePassword(@Auth User user, String password) {
        try {
            accountService.changePassword(user.getName(), password);
            return Response.ok().build();
        }
        catch (UserDoesNotExistException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @RolesAllowed(Roles.ADMIN)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{username}/roles")
    public Response changeRoles(@Auth User user, @PathParam("username") String username, String roles) {
        try {
            accountService.changeRoles(username, roles);
            return Response.ok().build();
        }
        catch (UserDoesNotExistException e) {
            String reason = String.format("User %s does not exist", username);
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), reason).build();
        }
    }
}
