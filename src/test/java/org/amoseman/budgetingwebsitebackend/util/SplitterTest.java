package org.amoseman.budgetingwebsitebackend.util;

import org.amoseman.budgetingwebsitebackend.pojo.bucket.Bucket;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SplitterTest {

    @Test
    void get() {
        final double BUCKET_SHARE_ONE = 0.496274986;
        final double BUCKET_SHARE_TWO = 0.1234356456;
        final double BUCKET_SHARE_THREE = 1.0 - BUCKET_SHARE_ONE - BUCKET_SHARE_TWO;
        final long AMOUNT = 100;

        assertEquals(1.0, BUCKET_SHARE_ONE + BUCKET_SHARE_TWO + BUCKET_SHARE_THREE);

        List<Bucket> buckets = new ArrayList<>();
        buckets.add(new Bucket(null, null, null, null, null, BUCKET_SHARE_ONE, 0));
        buckets.add(new Bucket(null, null, null, null, null, BUCKET_SHARE_TWO, 0));
        buckets.add(new Bucket(null, null, null, null, null, BUCKET_SHARE_THREE, 0));

        Split split = Splitter.get(buckets, AMOUNT);
        long sum = Splitter.sum(split.getAmounts());
        assertEquals(AMOUNT, sum);
    }
}