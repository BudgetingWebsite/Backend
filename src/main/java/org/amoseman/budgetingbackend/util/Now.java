package org.amoseman.budgetingbackend.util;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class Now {
    public static LocalDateTime get() {
        return LocalDateTime.now(ZoneId.of("UCT"));
    }
}
