package org.amoseman.budgetingwebsitebackend.resource;

import io.dropwizard.auth.Auth;
import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.amoseman.budgetingwebsitebackend.application.auth.User;

@Path("/stats")
@Produces(MediaType.APPLICATION_JSON)
public class StatisticsResource<C> {
    private final StatisticsService<C> statisticsService;

    public StatisticsResource(StatisticsService<C> statisticsService) {
        this.statisticsService = statisticsService;
    }

    @Path("/total")
    @GET
    @PermitAll
    public Response totalFunds(@Auth User user) {
        long total = statisticsService.totalFunds(user.getName());
        return Response.ok(total).build();
    }
}
