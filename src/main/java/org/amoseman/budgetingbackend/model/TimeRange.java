package org.amoseman.budgetingbackend.model;

import java.time.LocalDateTime;

/**
 * Represents a range of time.
 */
public class TimeRange {
    private final LocalDateTime start;
    private final LocalDateTime end;

    /**
     * Instantiate a time range.
     * @param start the start of the range.
     * @param end the end of the range.
     */
    public TimeRange(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }
}
