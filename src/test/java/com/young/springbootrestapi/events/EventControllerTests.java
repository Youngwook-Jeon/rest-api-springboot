package com.young.springbootrestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void createEvent() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API dev with Spring boot")
                .beginEnrollmentDateTime(LocalDateTime.of(2021, 7, 5, 13, 30))
                .closeEnrollmentDateTime(LocalDateTime.of(2021, 7, 6, 13, 30))
                .beginEventDateTime(LocalDateTime.of(2021, 7, 10, 13, 30))
                .endEventDateTime(LocalDateTime.of(2021, 7, 11, 13, 30))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(200)
                .location("신촌 스터디 카페")
                .build();

        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(Matchers.not(true)))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()));
    }

    @Test
    public void createEvent_BadRequest() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("REST API dev with Spring boot")
                .beginEnrollmentDateTime(LocalDateTime.of(2021, 7, 5, 13, 30))
                .closeEnrollmentDateTime(LocalDateTime.of(2021, 7, 6, 13, 30))
                .beginEventDateTime(LocalDateTime.of(2021, 7, 10, 13, 30))
                .endEventDateTime(LocalDateTime.of(2021, 7, 11, 13, 30))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(200)
                .location("신촌 스터디 카페")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
