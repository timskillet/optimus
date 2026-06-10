package org.timskillet.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class EventServiceTest {
    private EventService service;

    @BeforeEach
    void setUp() {
        service = new EventService();
    }

    @Test
    void saveAndRetrieveEvent() {
        Event event = new Event("payment-service", "ERROR", "DB timeout", Instant.parse("2026-06-10T10:00:00Z"));

        service.save(event);

        List<Event> events = service.findAll();
        assertThat(events).hasSize(1);
        assertThat(events.get(0).serviceName()).isEqualTo("payment-service");
    }

    @Test
    void findAllReturnsAllSavedEvents() {
        service.save(new Event("svc-a", "INFO", "started", Instant.now()));
        service.save(new Event("svc-b", "WARN", "slow", Instant.now()));

        assertThat(service.findAll()).hasSize(2);
    }

    @Test
    void findAllReturnsEmptyListInitially() {
        assertThat(service.findAll()).isEmpty();
    }
}
