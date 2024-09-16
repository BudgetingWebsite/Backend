package org.amoseman.budgetingwebsitebackend.resource;

import io.dropwizard.auth.Auth;
import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.amoseman.budgetingwebsitebackend.application.auth.User;
import org.amoseman.budgetingwebsitebackend.exception.FinanceRecordAlreadyExistsException;
import org.amoseman.budgetingwebsitebackend.exception.NegativeValueException;
import org.amoseman.budgetingwebsitebackend.pojo.event.Expense;
import org.amoseman.budgetingwebsitebackend.pojo.event.Income;
import org.amoseman.budgetingwebsitebackend.pojo.event.op.CreateExpense;
import org.amoseman.budgetingwebsitebackend.pojo.event.op.CreateIncome;
import org.amoseman.budgetingwebsitebackend.service.FinanceRecordService;

import java.time.DateTimeException;
import java.util.List;

@Path("/event")
@Produces(MediaType.APPLICATION_JSON)
public class FinanceRecordResource<C> {
    private final FinanceRecordService<C> financeRecordService;

    public FinanceRecordResource(FinanceRecordService<C> financeRecordService) {
        this.financeRecordService = financeRecordService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    @Path("/income")
    public Response addIncome(@Auth User user, CreateIncome create) {
        try {
            String uuid = financeRecordService.addIncome(user.getName(), create);
            return Response.ok(uuid).build();
        }
        catch (NegativeValueException e) {
            String reason = String.format("Negative amount: %s", create.getAmount());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), reason).build();
        }
        catch (FinanceRecordAlreadyExistsException e) {
            // todo: this should never occur, so make it re-attempt once in the service if the UUID already exists
            String reason = "Congratulations, you won the UUID v4 lottery as the UUID generated is already in use!";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), reason).build();
        }
        catch (DateTimeException e) {
            String reason = "Not a valid date";
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), reason).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    @Path("/expense")
    public Response addExpense(@Auth User user, CreateExpense create) {
        try {
            String uuid = financeRecordService.addExpense(user.getName(), create);
            return Response.ok(uuid).build();
        }
        catch (NegativeValueException e) {
            String reason = String.format("Negative amount: %s", create.getAmount());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), reason).build();
        }
        catch (FinanceRecordAlreadyExistsException e) {
            // todo: this should never occur, so make it re-attempt once in the service if the UUID already exists
            String reason = "Congratulations, you won the UUID v4 lottery as the UUID is already in use! ";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), reason).build();
        }
        catch (DateTimeException e) {
            String reason = "Not a valid date";
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), reason).build();
        }
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    @Path("/income/{uuid}")
    public Response removeIncome(@Auth User user, @PathParam("uuid") String uuid) {
        if (financeRecordService.removeIncome(user.getName(), uuid)) {
            return Response.ok().build();
        }
        String reason = String.format("Income record %s does not exist", uuid);
        return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), reason).build();
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    @Path("/expense/{uuid}")
    public Response removeExpense(@Auth User user, @PathParam("uuid") String uuid) {
        if (financeRecordService.removeExpense(user.getName(), uuid)) {
            return Response.ok().build();
        }
        String reason = String.format("Expense record %s does not exist", uuid);
        return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), reason).build();
    }

    @GET
    @PermitAll
    @Path("/income")
    public Response getIncome(
            @Auth User user,
            @DefaultValue("1") @QueryParam("start-year") String startYear,
            @DefaultValue("1") @QueryParam("start-month") String startMonth,
            @DefaultValue("1") @QueryParam("start-day") String startDay,
            @DefaultValue("9999") @QueryParam("end-year") String endYear,
            @DefaultValue("1") @QueryParam("end-month") String endMonth,
            @DefaultValue("1") @QueryParam("end-day") String endDay) {
        try {
            List<Income> income = financeRecordService.getIncome(
                    user.getName(),
                    startYear, startMonth, startDay,
                    endYear, endMonth, endDay
            );
            return Response.ok(income).build();
        }
        catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), "Invalid value in time range").build();
        }
    }

    @GET
    @PermitAll
    @Path("/expense")
    public Response getExpenses(
            @Auth User user,
            @DefaultValue("1") @QueryParam("start-year") String startYear,
            @DefaultValue("1") @QueryParam("start-month") String startMonth,
            @DefaultValue("1") @QueryParam("start-day") String startDay,
            @DefaultValue("9999") @QueryParam("end-year") String endYear,
            @DefaultValue("1") @QueryParam("end-month") String endMonth,
            @DefaultValue("1") @QueryParam("end-day") String endDay) {
        try {
            List<Expense> expenses = financeRecordService.getExpenses(
                    user.getName(),
                    startYear, startMonth, startDay,
                    endYear, endMonth, endDay
            );
            return Response.ok(expenses).build();
        }
        catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), "Invalid value in time range").build();
        }
    }
}
