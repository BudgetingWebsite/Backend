package org.amoseman.budgetingbackend.resource;

import io.dropwizard.auth.Auth;
import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.amoseman.budgetingbackend.application.auth.User;
import org.amoseman.budgetingbackend.exception.BucketAlreadyExistsException;
import org.amoseman.budgetingbackend.exception.BucketDoesNotExistException;
import org.amoseman.budgetingbackend.exception.TotalBucketShareExceededException;
import org.amoseman.budgetingbackend.pojo.bucket.Bucket;
import org.amoseman.budgetingbackend.pojo.bucket.op.CreateBucket;
import org.amoseman.budgetingbackend.pojo.bucket.op.UpdateBucket;
import org.amoseman.budgetingbackend.service.BucketService;

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
    public Response addBucket(@Auth User user, CreateBucket create) throws TotalBucketShareExceededException, BucketAlreadyExistsException {
        String uuid = bucketService.addBucket(user.getName(), create);
        return Response.ok(uuid).build();
    }

    @DELETE
    @PermitAll
    @Path("/{uuid}")
    public Response removeBucket(@Auth User user, @PathParam("uuid") String uuid) throws BucketDoesNotExistException {
        bucketService.removeBucket(user.getName(), uuid);
        return Response.ok().build();
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
    public Response updateBucket(@Auth User user, @PathParam("uuid") String uuid, UpdateBucket update) throws BucketDoesNotExistException {
        bucketService.updateBucket(user.getName(), uuid, update);
        return Response.ok().build();
    }
}
