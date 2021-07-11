package com.young.springbootrestapi.events;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

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

    @ParameterizedTest
    @MethodSource("paramsForTestFree")
    public void testFree(int basePrice, int maxPrice, boolean isFree) {
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();

        event.update();

        assertEquals(event.isFree(), isFree);
    }

    @ParameterizedTest
    @MethodSource("paramsForTestOffline")
    public void testOffline(String location, boolean isOffline) {
        Event event = Event.builder()
                .location(location)
                .build();

        event.update();

        assertEquals(event.isOffline(), isOffline);
    }

    private static Stream<Arguments> paramsForTestFree() {
        return Stream.of(
                Arguments.of(0, 0, true),
                Arguments.of(100, 0, false),
                Arguments.of(0, 100, false),
                Arguments.of(100, 200, false)
        );
    }

    private static Stream<Arguments> paramsForTestOffline() {
        return Stream.of(
                Arguments.of("신촌 스터디 카페", true),
                Arguments.of(null, false)
        );
    }
}