package org.amoseman.budgetingwebsitebackend.util;

import org.amoseman.budgetingwebsitebackend.pojo.partition.Partition;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SplitterTest {

    @Test
    void get() {
        for (int i = 0; i < 1000; i++) {
            double r = 1;
            double a = Math.random() * r;
            r -= a;
            double b = Math.random() * r;
            r -= b;
            double c = Math.random() * r;
            r -= c;
            double d = r;
            double[] shares = new double[]{a, b, c, d};
            List<Partition> partitions = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                partitions.add(new Partition(null, null, null, null, null, shares[i], 0));
            }
            long amount = (long) (Math.random() * 1000);
            Split split = Splitter.get(partitions, amount);
            long sum = Splitter.sum(split.getAmounts());
            assertEquals(amount, sum);
        }
    }
}