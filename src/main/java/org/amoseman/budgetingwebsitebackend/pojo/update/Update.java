package org.amoseman.budgetingwebsitebackend.pojo.update;

import java.time.LocalDateTime;

public class Update {
    private final LocalDateTime updated;

    public Update(LocalDateTime updated) {
        this.updated = updated;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }
}
