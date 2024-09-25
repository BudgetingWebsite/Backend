package org.amoseman.budgetingbackend.resource;

import io.dropwizard.auth.Auth;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.amoseman.budgetingbackend.application.auth.Roles;
import org.amoseman.budgetingbackend.application.auth.User;
import org.amoseman.budgetingbackend.exception.AccountAlreadyExistsException;
import org.amoseman.budgetingbackend.exception.AccountDoesNotExistException;
import org.amoseman.budgetingbackend.exception.UsernameExceedsMaxLengthException;
import org.amoseman.budgetingbackend.model.account.op.CreateAccount;
import org.amoseman.budgetingbackend.password.ResultType;
import org.amoseman.budgetingbackend.service.AccountService;

@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource<C> {
    private final AccountService<C> accountService;

    public AccountResource(AccountService<C> accountService) {
        this.accountService = accountService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAccount(CreateAccount account) throws AccountAlreadyExistsException, UsernameExceedsMaxLengthException {
        ResultType result = accountService.addAccount(account);
        return Response.ok(result).build();
    }

    @PermitAll
    @DELETE
    @Path("/{username}")
    public Response removeAccount(@Auth User user, @PathParam("username") String username) throws AccountDoesNotExistException {
        if (!user.getRoles().contains(Roles.ADMIN) && !user.getName().equals(username)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        accountService.removeAccount(username);
        return Response.ok().build();
    }

    @PermitAll
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/password")
    public Response changePassword(@Auth User user, String password) throws AccountDoesNotExistException {
        ResultType result = accountService.changePassword(user.getName(), password);
        return Response.ok(result).build();
    }

    @RolesAllowed({Roles.ADMIN})
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{username}/roles")
    public Response changeRoles(@Auth User user, @PathParam("username") String username, String roles) throws AccountDoesNotExistException {
        accountService.changeRoles(username, roles);
        return Response.ok().build();
    }
}
