package org.amoseman.budgetingbackend.util;

/**
 * Represents a dollar amount split into several amounts.
 */
public final class SplitCurrency {
    private final long[] amounts;
    private final long remainder;

    /**
     * Instantiate a new dollar amount split.
     * @param amounts the amounts it was split into.
     * @param remainder the remaining dollar amount.
     */
    SplitCurrency(long[] amounts, long remainder) {
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
