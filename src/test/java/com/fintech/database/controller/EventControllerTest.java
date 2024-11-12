package com.fintech.database.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fintech.database.TestContainers;
import com.fintech.database.dto.request.EventsRq;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(scripts = "/sql/insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestContainers.class)
@AutoConfigureMockMvc
class EventControllerTest {

    ObjectMapper mapper = new ObjectMapper();

    @LocalServerPort
    private Integer port;

    @Autowired
    MockMvc mockMvc;

    @WithMockUser
    @Test
    void getFilterEventsByLocationSuccess() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/events?locations=Moscow"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @WithAnonymousUser
    @Test
    void getFilterEventsByLocationWithAnonymousUserFail() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/events?locations=Moscow"))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void getFilterEventsByNameAndPastDateFail() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port +
                        "/api/v1/events?name=Festival&fromDate=2024-10-25&toDate=2024-12-31"))
                .andDo(print())
                .andExpectAll(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Not valid fields"))
                .andExpect(jsonPath("$.length()").value(3));
    }

    @WithMockUser
    @Test
    void getFilterEventsByNameAndDateSuccess() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port +
                        "/api/v1/events?name=Festival&fromDate=2025-10-25&toDate=2025-12-31"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @WithAnonymousUser
    @Test
    void getFilterEventsByNameAndDateWithAnonymousUserFail() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port +
                        "/api/v1/events?name=Festival&fromDate=2025-10-25&toDate=2025-12-31"))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void getAllEvents() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/events/all"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("Festival ONE"))
                .andExpect(jsonPath("$.data[1].name").value("Festival TWO"))
                .andExpect(jsonPath("$.data[2].name").value("Festival THREE"));
    }

    @WithAnonymousUser
    @Test
    void getAllEventsWithAnonymousUserFail() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/events/all"))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void getEventsRsByIdIsPresentSuccess() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/events/1"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Festival ONE"));
    }

    @WithAnonymousUser
    @Test
    void getEventsRsByIdIsPresentWithAnonymousUserFail() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/events/1"))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void getEventsRsByIdIsNotPresentFail() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/events/100"))
                .andDo(print())
                .andExpectAll(status().isNotFound());
    }

    @WithMockUser
    @Test
    void createEventsSuccess() throws Exception {
        EventsRq eventsRq = EventsRq.builder()
                .name("NewEvents")
                .dates(LocalDate.now())
                .locations("Moscow")
                .price("500")
                .build();
        mapper.registerModule(new JavaTimeModule());

        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(eventsRq)))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data.name").value("NewEvents"))
                .andExpect(jsonPath("$.data.price").value("500"))
                .andExpect(jsonPath("$.data.locations.slug").value("msk"));
    }

    @WithAnonymousUser
    @Test
    void createEventsWithAnonymousUserFail() throws Exception {
        EventsRq eventsRq = EventsRq.builder()
                .name("NewEvents")
                .dates(LocalDate.now())
                .locations("Moscow")
                .price("500")
                .build();
        mapper.registerModule(new JavaTimeModule());

        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(eventsRq)))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void createEventsWithIsNotPresentLocationsFail() throws Exception {
        EventsRq eventsRq = EventsRq.builder()
                .name("NewEvents")
                .dates(LocalDate.now())
                .locations("Kazan")
                .price("500")
                .build();
        mapper.registerModule(new JavaTimeModule());

        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(eventsRq)))
                .andDo(print())
                .andExpectAll(status().isBadRequest());
    }

    @WithMockUser
    @Test
    void createEventsWithNotValidFieldsDatesFail() throws Exception {
        EventsRq eventsRq = EventsRq.builder()
                .name("Name")
                .dates(LocalDate.now().minusYears(5))
                .locations("Moscow")
                .price("500")
                .build();
        mapper.registerModule(new JavaTimeModule());

        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(eventsRq)))
                .andDo(print())
                .andExpectAll(status().isBadRequest());
    }

    @WithMockUser
    @Test
    void updateEventsSuccess() throws Exception {
        EventsRq eventsRq = EventsRq.builder()
                .name("UpdateEvents")
                .build();

        this.mockMvc.perform(put("http://localhost:" + port + "/api/v1/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(eventsRq)))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data.name").value("UpdateEvents"))
                .andExpect(jsonPath("$.data.price").value("500"))
                .andExpect(jsonPath("$.data.locations.slug").value("msk"));
    }

    @WithAnonymousUser
    @Test
    void updateEventsWithAnonymousUserFail() throws Exception {
        EventsRq eventsRq = EventsRq.builder()
                .name("UpdateEvents")
                .build();

        this.mockMvc.perform(put("http://localhost:" + port + "/api/v1/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(eventsRq)))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void updateEventsWithIsNotPresentLocationsFail() throws Exception {
        EventsRq eventsRq = EventsRq.builder()
                .locations("Kazan")
                .build();

        this.mockMvc.perform(put("http://localhost:" + port + "/api/v1/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(eventsRq)))
                .andDo(print())
                .andExpectAll(status().isBadRequest());
    }

    @WithMockUser
    @Test
    void deleteEventsSuccess() throws Exception {
        this.mockMvc.perform(delete("http://localhost:" + port + "/api/v1/events/1"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data").value("Events with id 1 delete."));
    }

    @WithAnonymousUser
    @Test
    void deleteEventsWithAnonymousUserFail() throws Exception {
        this.mockMvc.perform(delete("http://localhost:" + port + "/api/v1/events/1"))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void deleteEventsIsNotPresentIdFail() throws Exception {
        this.mockMvc.perform(delete("http://localhost:" + port + "/api/v1/events/100"))
                .andDo(print())
                .andExpectAll(status().isNotFound());
    }
}