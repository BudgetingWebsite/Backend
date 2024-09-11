package org.amoseman.budgetingwebsitebackend.util;

import org.junit.jupiter.api.Test;

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
            long amount = (long) (Math.random() * 1000);
            Split split = Splitter.get(shares, amount);
            long sum = Splitter.sum(split.getAmounts());
            assertEquals(amount, sum);
        }
    }
}