package org.timskillet.event;

import java.time.Instant;

public record Event(
        String serviceName,
        String severity,
        String message,
        Instant timestamp
) {
}
