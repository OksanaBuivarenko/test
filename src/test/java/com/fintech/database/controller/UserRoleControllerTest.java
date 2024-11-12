package com.fintech.database.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fintech.database.TestContainers;
import com.fintech.database.dto.request.UserRoleRq;
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
class UserRoleControllerTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    MockMvc mockMvc;

    @WithMockUser(authorities = {"ROLE_ADMIN"})
    @Test
    void getAllRolesWithAdminRoleAuthSuccess() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/roles"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("ROLE_USER"))
                .andExpect(jsonPath("$.data[1].name").value("ROLE_ADMIN"))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @WithMockUser(authorities = {"ROLE_USER"})
    @Test
    void getAllRolesWithUserRoleAuthFail() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/roles"))
                .andDo(print())
                .andExpectAll(status().isForbidden());
    }

    @WithAnonymousUser
    @Test
    void getAllRolesWithAnonymousUserFail() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/roles"))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @WithMockUser(authorities = {"ROLE_ADMIN"})
    @Test
    void getRolesRsByIdWithAdminRoleAuthSuccess() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/roles/1"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data.name").value("ROLE_USER"))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @WithMockUser(authorities = {"ROLE_USER"})
    @Test
    void getRolesRsByIdWithUserRoleAuthFail() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/roles/1"))
                .andDo(print())
                .andExpectAll(status().isForbidden());
    }

    @WithAnonymousUser
    @Test
    void getRolesRsByIdWithAnonymousUserFail() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/roles/1"))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    public String getUserRoleRq() throws JsonProcessingException {
        UserRoleRq userRoleRq = UserRoleRq.builder().name("ROLE_NEW").build();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(userRoleRq);
    }

    @WithMockUser(authorities = {"ROLE_ADMIN"})
    @Test
    void createRoleWithAdminRoleAuthSuccess() throws Exception {
        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getUserRoleRq()))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data.name").value("ROLE_NEW"));
    }

    @WithMockUser(authorities = {"ROLE_USER"})
    @Test
    void createRoleWithUserRoleAuthFail() throws Exception {
        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getUserRoleRq()))
                .andDo(print())
                .andExpectAll(status().isForbidden());
    }

    @WithAnonymousUser
    @Test
    void createRoleWithAnonymousUserFail() throws Exception {
        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getUserRoleRq()))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @WithMockUser(authorities = {"ROLE_ADMIN"})
    @Test
    void updateRoleWithAdminRoleAuthSuccess() throws Exception {
        this.mockMvc.perform(put("http://localhost:" + port + "/api/v1/roles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getUserRoleRq()))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data.name").value("ROLE_NEW"));
    }

    @WithMockUser(authorities = {"ROLE_USER"})
    @Test
    void updateRoleWithUserRoleAuthFail() throws Exception {
        this.mockMvc.perform(put("http://localhost:" + port + "/api/v1/roles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getUserRoleRq()))
                .andDo(print())
                .andExpectAll(status().isForbidden());
    }

    @WithAnonymousUser
    @Test
    void updateRoleWithAnonymousUserFail() throws Exception {
        this.mockMvc.perform(put("http://localhost:" + port + "/api/v1/roles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getUserRoleRq()))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @WithMockUser(authorities = {"ROLE_ADMIN"})
    @Test
    void deleteRoleWithAdminRoleAuthSuccess() throws Exception {
        this.mockMvc.perform(delete("http://localhost:" + port + "/api/v1/roles/1"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data").value("User role with id 1 delete."))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @WithMockUser(authorities = {"ROLE_USER"})
    @Test
    void deleteRoleWithUserRoleAuthFail() throws Exception {
        this.mockMvc.perform(delete("http://localhost:" + port + "/api/v1/roles/1"))
                .andDo(print())
                .andExpectAll(status().isForbidden());
    }

    @WithAnonymousUser
    @Test
    void deleteRoleWithAnonymousUserFail() throws Exception {
        this.mockMvc.perform(delete("http://localhost:" + port + "/api/v1/roles/1"))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }
}