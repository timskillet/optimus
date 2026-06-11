package org.timskillet.event;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EventService eventService;

    @Test
    void postEventReturns201() throws Exception {
        Event event = new Event("payment-service", "ERROR", "DB timeout", Instant.parse("2026-06-10T10:00:00Z"));
        doNothing().when(eventService).save(any(Event.class));

        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isCreated());

        verify(eventService).save(any(Event.class));
    }

    @Test
    void getEventsReturnsAllEvents() throws Exception {
        List<Event> events = List.of(
                new Event("svc-a", "INFO", "started", Instant.parse("2026-06-10T09:00:00Z")),
                new Event("svc-b", "ERROR", "crashed", Instant.parse("2026-06-10T10:00:00Z"))
        );
        when(eventService.findAll()).thenReturn(events);

        mockMvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].serviceName").value("svc-a"))
                .andExpect(jsonPath("$[1].severity").value("ERROR"));
    }

    @Test
    void getEventsReturnsEmptyArrayWhenNoEvents() throws Exception {
        when(eventService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}