package org.amoseman.budgetingwebsitebackend.resource;

import io.dropwizard.auth.Auth;
import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.amoseman.budgetingwebsitebackend.application.auth.User;
import org.amoseman.budgetingwebsitebackend.exception.PartitionAlreadyExistsException;
import org.amoseman.budgetingwebsitebackend.exception.PartitionDoesNotExistException;
import org.amoseman.budgetingwebsitebackend.pojo.partition.Partition;
import org.amoseman.budgetingwebsitebackend.pojo.partition.op.CreatePartition;
import org.amoseman.budgetingwebsitebackend.pojo.partition.op.UpdatePartition;
import org.amoseman.budgetingwebsitebackend.service.PartitionService;

import java.util.List;

@Path("/partition")
@Produces(MediaType.APPLICATION_JSON)
public class PartitionResource<C> {
    private final PartitionService<C> partitionService;

    public PartitionResource(PartitionService<C> partitionService) {
        this.partitionService = partitionService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response addPartition(@Auth User user, CreatePartition create) {
        try {
            partitionService.addPartition(user.getName(), create);
            return Response.ok().build();
        }
        catch (PartitionAlreadyExistsException e) {
            // todo: this should never occur, so make it re-attempt once in the service if the UUID already exists
            String reason = "Congratulations, you won the UUID v4 lottery as the UUID generated is already in use!";
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), reason).build();
        }
    }

    @DELETE
    @PermitAll
    @Path("/{id}")
    public Response removePartition(@Auth User user, @PathParam("id") String id) {
        try {
            partitionService.removePartition(user.getName(), id);
            return Response.ok().build();
        }
        catch (PartitionDoesNotExistException e) {
            String reason = String.format("Partition %s does not exist", id);
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), reason).build();
        }
    }

    @GET
    @PermitAll
    public Response getPartitions(@Auth User user) {
        List<Partition> partitions = partitionService.listPartitions(user.getName());
        return Response.ok(partitions).build();
    }

    @PUT
    @PermitAll
    @Path("/{uuid}")
    public Response updatePartition(@Auth User user, @PathParam("uuid") String uuid, UpdatePartition update) {
        try {
            partitionService.updatePartition(user.getName(), uuid, update);
            return Response.ok().build();
        }
        catch (PartitionDoesNotExistException e) {
            String reason = String.format("Partition %s does not exist", uuid);
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), reason).build();
        }
    }
}
