package org.amoseman.budgetingbackend.util;

import org.amoseman.budgetingbackend.pojo.bucket.Bucket;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides dollar amount splitting functionality.
 */
public final class Splitter {

    /**
     * Split the provided amount based on the provided buckets.
     * @param buckets the buckets.
     * @param amount the dollar amount to split.
     * @return the split amount.
     */
    public static Split get(List<Bucket> buckets, long amount) {
        int len = buckets.size();
        double[] floored = new double[len];
        double[] differences = new double[len];
        double remainder = 0;
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            double raw = buckets.get(i).share * amount;
            floored[i] = Math.floor(raw);
            differences[i] = raw - floored[i];
            remainder += differences[i];
            indices.add(i);
        }
        sortIndices(indices, differences);

        long r = Math.round(remainder);
        long[] out = new long[len];
        long total = 0;
        for (Integer index : indices) {
            out[index] = (long) floored[index];
            if (r > 0) {
                r--;
                out[index]++;
            }
            total += out[index];
        }
        return new Split(out, amount - total);
    }

    public static void sortIndices(List<Integer> indices, double[] differences) {
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
        indices.addAll(sortedIndices);
    }

    public static long sum(long[] x) {
        long y = 0;
        for (long e : x) {
            y += e;
        }
        return y;
    }
}
