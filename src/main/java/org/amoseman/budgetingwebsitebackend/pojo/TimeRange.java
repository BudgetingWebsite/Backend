package org.amoseman.budgetingwebsitebackend.pojo;

import java.time.LocalDateTime;

public class TimeRange {
    private final LocalDateTime start;
    private final LocalDateTime end;

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
