package org.amoseman.budgetingbackend.resource;

import io.dropwizard.auth.Auth;
import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.amoseman.budgetingbackend.application.auth.User;
import org.amoseman.budgetingbackend.exception.FinanceRecordAlreadyExistsException;
import org.amoseman.budgetingbackend.exception.FinanceRecordDoesNotExistException;
import org.amoseman.budgetingbackend.exception.NegativeValueException;
import org.amoseman.budgetingbackend.pojo.record.Expense;
import org.amoseman.budgetingbackend.pojo.record.Income;
import org.amoseman.budgetingbackend.pojo.record.info.ExpenseInfo;
import org.amoseman.budgetingbackend.pojo.record.info.IncomeInfo;
import org.amoseman.budgetingbackend.service.FinanceRecordService;

import java.time.DateTimeException;
import java.util.List;

@Path("/record")
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
    public Response addIncome(@Auth User user, IncomeInfo create) throws NegativeValueException, FinanceRecordAlreadyExistsException, DateTimeException {
        String uuid = financeRecordService.addIncome(user.getName(), create);
        return Response.ok(uuid).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    @Path("/expense")
    public Response addExpense(@Auth User user, ExpenseInfo create) throws NegativeValueException, FinanceRecordAlreadyExistsException, DateTimeException {
        String uuid = financeRecordService.addExpense(user.getName(), create);
        return Response.ok(uuid).build();
    }

    @DELETE
    @PermitAll
    @Path("/income/{uuid}")
    public Response removeIncome(@Auth User user, @PathParam("uuid") String uuid) throws FinanceRecordDoesNotExistException {
        if (!financeRecordService.removeIncome(user.getName(), uuid)) {
            throw new FinanceRecordDoesNotExistException("remove", uuid);
        }
        return Response.ok().build();
    }

    @DELETE
    @PermitAll
    @Path("/expense/{uuid}")
    public Response removeExpense(@Auth User user, @PathParam("uuid") String uuid) throws FinanceRecordDoesNotExistException {
        if (!financeRecordService.removeExpense(user.getName(), uuid)) {
            throw new FinanceRecordDoesNotExistException("remove", uuid);
        }
        return Response.ok().build();
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
            @DefaultValue("1") @QueryParam("end-day") String endDay) throws NumberFormatException {
        List<Income> income = financeRecordService.getIncome(
                user.getName(),
                startYear, startMonth, startDay,
                endYear, endMonth, endDay
        );
        return Response.ok(income).build();
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
            @DefaultValue("1") @QueryParam("end-day") String endDay) throws NumberFormatException {
        List<Expense> expenses = financeRecordService.getExpenses(
                user.getName(),
                startYear, startMonth, startDay,
                endYear, endMonth, endDay
        );
        return Response.ok(expenses).build();
    }

    @PUT
    @PermitAll
    @Path("/income/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateIncome(@Auth User user, @PathParam("uuid") String uuid, IncomeInfo update) throws NegativeValueException, FinanceRecordDoesNotExistException {
        financeRecordService.updateIncome(user.getName(), uuid, update);
        return Response.ok().build();
    }

    @PUT
    @PermitAll
    @Path("/expense/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateExpense(@Auth User user, @PathParam("uuid") String uuid, ExpenseInfo update) throws NegativeValueException, FinanceRecordDoesNotExistException {
        financeRecordService.updateExpense(user.getName(), uuid, update);
        return Response.ok().build();
    }
}
