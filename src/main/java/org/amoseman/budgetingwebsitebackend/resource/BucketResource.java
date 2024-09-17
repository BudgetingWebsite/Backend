package org.amoseman.budgetingwebsitebackend.resource;

import io.dropwizard.auth.Auth;
import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.amoseman.budgetingwebsitebackend.application.auth.User;
import org.amoseman.budgetingwebsitebackend.exception.BucketAlreadyExistsException;
import org.amoseman.budgetingwebsitebackend.exception.BucketDoesNotExistException;
import org.amoseman.budgetingwebsitebackend.exception.TotalBucketShareExceededException;
import org.amoseman.budgetingwebsitebackend.pojo.bucket.Bucket;
import org.amoseman.budgetingwebsitebackend.pojo.bucket.op.CreateBucket;
import org.amoseman.budgetingwebsitebackend.pojo.bucket.op.UpdateBucket;
import org.amoseman.budgetingwebsitebackend.service.BucketService;

import java.util.List;

@Path("/bucket")
@Produces(MediaType.APPLICATION_JSON)
public class BucketResource<C> {
    private final BucketService<C> bucketService;

    public BucketResource(BucketService<C> bucketService) {
        this.bucketService = bucketService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response addBucket(@Auth User user, CreateBucket create) {
        try {
            String uuid = bucketService.addBucket(user.getName(), create);
            return Response.ok(uuid).build();
        }
        catch (BucketAlreadyExistsException e) {
            // todo: this should never occur, so make it re-attempt once in the service if the UUID already exists
            String reason = "Congratulations, you won the UUID v4 lottery as the UUID generated is already in use!";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), reason).build();
        }
        catch (TotalBucketShareExceededException e) {
            String reason = "Attempted to add bucket that would cause the current share total to be exceeded";
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), reason).build();
        }
    }

    @DELETE
    @PermitAll
    @Path("/{uuid}")
    public Response removeBucket(@Auth User user, @PathParam("id") String id) {
        try {
            bucketService.removeBucket(user.getName(), id);
            return Response.ok().build();
        }
        catch (BucketDoesNotExistException e) {
            String reason = String.format("Bucket %s does not exist", id);
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), reason).build();
        }
    }

    @GET
    @PermitAll
    public Response getBuckets(@Auth User user) {
        List<Bucket> buckets = bucketService.getBuckets(user.getName());
        return Response.ok(buckets).build();
    }

    @PUT
    @PermitAll
    @Path("/{uuid}")
    public Response updateBucket(@Auth User user, @PathParam("uuid") String uuid, UpdateBucket update) {
        try {
            bucketService.updateBucket(user.getName(), uuid, update);
            return Response.ok().build();
        }
        catch (BucketDoesNotExistException e) {
            String reason = String.format("Bucket %s does not exist", uuid);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), reason).build();
        }
    }
}
