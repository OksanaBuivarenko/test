package com.fintech.database.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fintech.database.TestContainers;
import com.fintech.database.dto.request.LocationRq;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(scripts = "/sql/insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestContainers.class)
@AutoConfigureMockMvc
class LocationsControllerTest {
    ObjectMapper mapper = new ObjectMapper();

    @LocalServerPort
    private Integer port;

    @Autowired
    MockMvc mockMvc;

    @WithMockUser
    @Test
    void getLocationsSuccess() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/locations"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("Saint Petersburg"))
                .andExpect(jsonPath("$.data[1].name").value("Moscow"));
    }

    @WithAnonymousUser
    @Test
    void getLocationsWithAnonymousUserFail() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/locations"))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void getLocationByIdIsPresentSuccess() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/locations/1"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Saint Petersburg"))
                .andExpect(jsonPath("$.data.slug").value("spb"));
    }

    @WithAnonymousUser
    @Test
    void getLocationByIdIsPresentWithAnonymousUserFail() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/locations/1"))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void getLocationByIdIsNotPresentFail() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/locations/100"))
                .andDo(print())
                .andExpectAll(status().isNotFound());
    }

    @WithMockUser
    @Test
    void createLocationSuccess() throws Exception {
        LocationRq locationRq = LocationRq.builder()
                .name("Location")
                .slug("loc")
                .build();

        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(locationRq)))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Location"))
                .andExpect(jsonPath("$.data.slug").value("loc"));
    }

    @WithAnonymousUser
    @Test
    void createLocationWithAnonymousUserFail() throws Exception {
        LocationRq locationRq = LocationRq.builder()
                .name("Location")
                .slug("loc")
                .build();

        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(locationRq)))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void createLocationWithNotValidFieldsFail() throws Exception {
        LocationRq locationRq = LocationRq.builder()
                .name("")
                .slug("")
                .build();

        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(locationRq)))
                .andDo(print())
                .andExpectAll(status().isBadRequest());
    }

    @WithMockUser
    @Test
    void updateLocationSuccess() throws Exception {
        LocationRq locationRq = LocationRq.builder()
                .name("Location")
                .slug("loc")
                .build();

        this.mockMvc.perform(put("http://localhost:" + port + "/api/v1/locations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(locationRq)))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Location"))
                .andExpect(jsonPath("$.data.slug").value("loc"));
    }

    @WithAnonymousUser
    @Test
    void updateLocationWithAnonymousUserFail() throws Exception {
        LocationRq locationRq = LocationRq.builder()
                .name("Location")
                .slug("loc")
                .build();

        this.mockMvc.perform(put("http://localhost:" + port + "/api/v1/locations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(locationRq)))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void deleteLocationIsPresentIdSuccess() throws Exception {
        this.mockMvc.perform(delete("http://localhost:" + port + "/api/v1/locations/1"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data").value("Locations with id 1 delete."));
    }

    @WithAnonymousUser
    @Test
    void deleteLocationIsPresentIdWithAnonymousUserFail() throws Exception {
        this.mockMvc.perform(delete("http://localhost:" + port + "/api/v1/locations/1"))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void deleteLocationIsNotPresentIdFail() throws Exception {
        this.mockMvc.perform(delete("http://localhost:" + port + "/api/v1/locations/100"))
                .andDo(print())
                .andExpectAll(status().isNotFound());
    }
}