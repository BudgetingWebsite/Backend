package org.amoseman.budgetingwebsitebackend.resource;

import io.dropwizard.auth.Auth;
import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.amoseman.budgetingwebsitebackend.application.auth.User;
import org.amoseman.budgetingwebsitebackend.exception.FinanceEventAlreadyExistsException;
import org.amoseman.budgetingwebsitebackend.exception.FinanceEventDoesNotExistException;
import org.amoseman.budgetingwebsitebackend.exception.InvalidFinanceEventTypeException;
import org.amoseman.budgetingwebsitebackend.exception.NegativeValueException;
import org.amoseman.budgetingwebsitebackend.pojo.event.FinanceEvent;
import org.amoseman.budgetingwebsitebackend.pojo.event.op.CreateExpenseEvent;
import org.amoseman.budgetingwebsitebackend.pojo.event.op.CreateIncomeEvent;
import org.amoseman.budgetingwebsitebackend.service.FinanceEventService;

import java.time.DateTimeException;
import java.util.List;

@Path("/event")
@Produces(MediaType.APPLICATION_JSON)
public class FinanceEventResource<C> {
    private final FinanceEventService<C> financeEventService;

    public FinanceEventResource(FinanceEventService<C> financeEventService) {
        this.financeEventService = financeEventService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    @Path("/income")
    public Response addEvent(@Auth User user, CreateIncomeEvent event) {
        try {
            String uuid = financeEventService.addEvent(user.getName(), event);
            return Response.ok(uuid).build();
        }
        catch (NegativeValueException e) {
            String reason = String.format("Negative amount: %s", event.getAmount());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), reason).build();
        }
        catch (FinanceEventAlreadyExistsException e) {
            // todo: this should never occur, so make it re-attempt once in the service if the UUID already exists
            String reason = "Congratulations, you won the UUID v4 lottery as the UUID generated is already in use!";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), reason).build();
        }
        catch (InvalidFinanceEventTypeException e) {
            String reason = String.format("%s is not a valid finance event type", event.getType());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), reason).build();
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
    public Response addEvent(@Auth User user, CreateExpenseEvent event) {
        try {
            String uuid = financeEventService.addEvent(user.getName(), event);
            return Response.ok(uuid).build();
        }
        catch (NegativeValueException e) {
            String reason = String.format("Negative amount: %s", event.getAmount());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), reason).build();
        }
        catch (FinanceEventAlreadyExistsException e) {
            // todo: this should never occur, so make it re-attempt once in the service if the UUID already exists
            String reason = "Congratulations, you won the UUID v4 lottery as the UUID is already in use! ";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), reason).build();
        }
        catch (InvalidFinanceEventTypeException e) {
            String reason = String.format("%s is not a valid finance event type", event.getType());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), reason).build();
        }
        catch (DateTimeException e) {
            String reason = "Not a valid date";
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), reason).build();
        }
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    @Path("/{type}/{uuid}")
    public Response removeEvent(@Auth User user, @PathParam("type") String type, @PathParam("uuid") String uuid) {
        try {
            financeEventService.removeEvent(user.getName(), uuid, type);
            return Response.ok().build();
        }
        catch (FinanceEventDoesNotExistException e) {
            String reason = String.format("Event %s does not exist", uuid);
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), reason).build();
        }
    }

    @GET
    @PermitAll
    @Path("/{type}")
    public Response getEvents(
            @Auth User user,
            @PathParam("type") String type,
            @DefaultValue("1") @QueryParam("start-year") String startYear,
            @DefaultValue("1") @QueryParam("start-month") String startMonth,
            @DefaultValue("1") @QueryParam("start-day") String startDay,
            @DefaultValue("9999") @QueryParam("end-year") String endYear,
            @DefaultValue("1") @QueryParam("end-month") String endMonth,
            @DefaultValue("1") @QueryParam("end-day") String endDay
            ) {
        try {
            List<FinanceEvent> events = financeEventService.getEvents(
                    user.getName(), type,
                    startYear, startMonth, startDay,
                    endYear, endMonth, endDay
                    );
            return Response.ok(events).build();
        }
        catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), "Invalid value in time range").build();
        }
    }
}
