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
        long[] rounded = new long[len];
        double[] loss = new double[len];
        double totalLoss = 0;
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            double raw = buckets.get(i).share * amount;
            rounded[i] = Math.round(raw);
            loss[i] = raw - rounded[i];
            totalLoss += loss[i];
            indices.add(i);
        }
        indices = sortIndices(indices, loss);
        return toSplit(rounded, indices, amount, totalLoss);
    }

    private static Split toSplit(long[] rounded, List<Integer> indices, long remainder, double totalLoss) {
        int index = 0;
        long remainingLoss = (long) Math.floor(totalLoss);
        while (remainingLoss > 0 && index < indices.size()) {
            remainingLoss--;
            index++;
            rounded[indices.get(index)]++;
            remainder -= rounded[index];
        }
        return new Split(rounded, remainder);
    }

    /**
     * Sort a list of indices by a provided array of loss.
     * @param indices the indices.
     * @param loss the loss.
     * @return the sorted list of indices.
     */
    public static List<Integer> sortIndices(List<Integer> indices, double[] loss) {
        return IntStream.range(0, indices.size())
                .boxed()
                .sorted(Comparator.comparingDouble(i -> loss[i]))
                .sorted(Comparator.reverseOrder())
                .map(indices::get)
                .toList();
    }

    /**
     * Sum the array of long.
     * @param x the array.
     * @return the sum.
     */
    public static long sum(long[] x) {
        long y = 0;
        for (long e : x) {
            y += e;
        }
        return y;
    }
}
