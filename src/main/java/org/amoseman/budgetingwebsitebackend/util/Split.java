package org.amoseman.budgetingwebsitebackend.util;

public class Split {
    private final long[] amounts;
    private final long remainder;

    public Split(long[] amounts, long remainder) {
        this.amounts = amounts;
        this.remainder = remainder;
    }

    public long[] getAmounts() {
        return amounts;
    }

    public long getRemainder() {
        return remainder;
    }
}
