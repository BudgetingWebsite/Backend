package org.amoseman.budgetingwebsitebackend.resource;

import io.dropwizard.auth.Auth;
import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.amoseman.budgetingwebsitebackend.application.auth.Roles;
import org.amoseman.budgetingwebsitebackend.application.auth.User;
import org.amoseman.budgetingwebsitebackend.exception.UserAlreadyExistsException;
import org.amoseman.budgetingwebsitebackend.exception.UserDoesNotExistException;
import org.amoseman.budgetingwebsitebackend.pojo.CreateAccount;
import org.amoseman.budgetingwebsitebackend.service.AccountService;

@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource<C> {
    private final AccountService<C> accountService;

    public AccountResource(AccountService<C> accountService) {
        this.accountService = accountService;
    }

    @POST
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
}
