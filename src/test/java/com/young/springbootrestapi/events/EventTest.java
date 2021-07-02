package com.young.springbootrestapi.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    @Test
    public void builder() {
        Event event = Event.builder()
                .name("Spring REST API")
                .description("REST API dev with Spring Boot")
                .build();
        assertNotNull(event);
    }

    @Test
    public void javaBean() {
        Event event = new Event();
        String name = "Event";
        String description = "Spring";
        event.setName(name);
        event.setDescription(description);

        assertEquals(event.getName(), name);
        assertEquals(event.getDescription(), description);
    }
}