package org.amoseman.budgetingwebsitebackend.util;

import java.util.ArrayList;
import java.util.List;

public class Splitter {
    public static Split get(double[] shares, long amount) {
        int len = shares.length;
        double[] floored = new double[len];
        double[] differences = new double[len];
        double remainder = 0;
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            double raw = shares[i] * amount;
            floored[i] = Math.floor(raw);
            differences[i] = raw - floored[i];
            remainder += differences[i];
            indices.add(i);
        }

        List<Integer> sortedIndices = new ArrayList<>();
        while (!indices.isEmpty()) {
            int largest = 0;
            for (int i = 1; i < indices.size(); i++) {
                Integer largestIndex = indices.get(largest);
                Integer otherIndex = indices.get(i);
                if (differences[largestIndex] < differences[otherIndex]) {
                    largest = i;
                }
            }
            sortedIndices.add(indices.remove(largest));
        }
        long r = Math.round(remainder);
        long[] out = new long[len];
        long total = 0;
        for (Integer index : sortedIndices) {
            System.out.println(differences[index]);
            out[index] = (long) floored[index];
            if (r > 0) {
                r--;
                out[index]++;
            }
            total += out[index];
        }
        return new Split(out, amount - total);
    }

    public static long sum(long[] x) {
        long y = 0;
        for (long e : x) {
            y += e;
        }
        return y;
    }
}
