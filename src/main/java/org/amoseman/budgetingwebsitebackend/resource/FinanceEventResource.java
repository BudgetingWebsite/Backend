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
import org.amoseman.budgetingwebsitebackend.pojo.CreateFinanceEvent;
import org.amoseman.budgetingwebsitebackend.pojo.FinanceEvent;
import org.amoseman.budgetingwebsitebackend.pojo.RemoveFinanceEvent;
import org.amoseman.budgetingwebsitebackend.service.FinanceEventService;

import java.util.List;

@Path("/event")
@Produces(MediaType.APPLICATION_JSON)
public class FinanceEventResource<C> {
    private final FinanceEventService<C> financeEventService;

    public FinanceEventResource(FinanceEventService<C> financeEventService) {
        this.financeEventService = financeEventService;
    }

    @POST
    @PermitAll
    public Response addEvent(@Auth User user, CreateFinanceEvent event) {
        try {
            financeEventService.addEvent(user.getName(), event);
            return Response.ok().build();
        }
        catch (NegativeValueException e) {
            String reason = String.format("Negative amount: %s", event.getAmount());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), reason).build();
        } catch (FinanceEventAlreadyExistsException e) {
            // todo: this should never occur, so make it re-attempt once in the service if the UUID already exists
            String reason = "Congratulations, you won the UUID v4 lottery as the UUID generated is already in use!";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), reason).build();
        } catch (InvalidFinanceEventTypeException e) {
            String reason = String.format("%s is not a valid finance event type", event.getType());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), reason).build();
        }
    }

    @DELETE
    @PermitAll
    public Response removeEvent(@Auth User user, RemoveFinanceEvent event) {
        try {
            financeEventService.removeEvent(user.getName(), event);
            return Response.ok().build();
        }
        catch (FinanceEventDoesNotExistException e) {
            String reason = String.format("Event %s does not exist", event.getId());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), reason).build();
        }
    }

    @GET
    @PermitAll
    @Path("/{type}")
    public Response getEvents(
            @Auth User user,
            @PathParam("type") String type,
            @DefaultValue("0") @QueryParam("start-year") String startYear,
            @DefaultValue("0") @QueryParam("start-month") String startMonth,
            @DefaultValue("0") @QueryParam("start-day") String startDay,
            @DefaultValue("9999") @QueryParam("end-year") String endYear,
            @DefaultValue("0") @QueryParam("end-month") String endMonth,
            @DefaultValue("0") @QueryParam("end-day") String endDay
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
