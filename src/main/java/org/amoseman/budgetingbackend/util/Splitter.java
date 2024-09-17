package org.amoseman.budgetingbackend.util;

import org.amoseman.budgetingbackend.pojo.bucket.Bucket;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

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
        double[] rounded = new double[len];
        double[] differences = new double[len];
        double remainder = 0;
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            double raw = buckets.get(i).share * amount;
            rounded[i] = Math.floor(raw);
            differences[i] = raw - rounded[i];
            remainder += differences[i];
            indices.add(i);
        }
        indices = sortIndices(indices, differences);

        long[] out = new long[len];
        long r = amount;
        int index = 0;
        long remainderRemaining = (long) Math.floor(remainder);
        while (remainderRemaining > 0 && index < indices.size()) {
            remainderRemaining--;
            index++;
            out[indices.get(index)]++;
            r -= out[index];
        }
        return new Split(out, r);
    }

    public static List<Integer> sortIndices(List<Integer> indices, double[] differences) {
        return IntStream.range(0, indices.size())
                .boxed()
                .sorted(Comparator.comparingDouble(i -> differences[i]))
                .sorted(Comparator.reverseOrder())
                .map(indices::get)
                .toList();
    }

    public static long sum(long[] x) {
        long y = 0;
        for (long e : x) {
            y += e;
        }
        return y;
    }
}
