package org.timskillet.event;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class EventService {
    private final List<Event> store = new ArrayList<>();

    public void save(Event event) {
        store.add(event);
    }

    public List<Event> findAll() {
        return Collections.unmodifiableList(store);
    }
}
