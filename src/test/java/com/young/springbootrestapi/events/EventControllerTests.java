package com.young.springbootrestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.young.springbootrestapi.commons.TestDescription;
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
    @TestDescription("정상적으로 이벤트를 생성하는 테스트")
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
    @TestDescription("입력 받을 수 없는 값을 사용할 경우에 에러가 발생하는 테스트")
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

    @Test
    @TestDescription("입력값이 비어있는 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("입력값이 잘못된 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API dev with Spring boot")
                .beginEnrollmentDateTime(LocalDateTime.of(2021, 7, 5, 13, 30))
                .closeEnrollmentDateTime(LocalDateTime.of(2021, 7, 6, 13, 30))
                .beginEventDateTime(LocalDateTime.of(2021, 7, 10, 13, 30))
                .endEventDateTime(LocalDateTime.of(2021, 7, 9, 13, 30))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(200)
                .location("신촌 스터디 카페")
                .build();

        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].code").exists());
    }
}
