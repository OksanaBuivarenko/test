package com.fintech.database.controller;

import com.fintech.database.TestContainers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(scripts = "/sql/insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestContainers.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    MockMvc mockMvc;

    @WithMockUser(authorities = {"ROLE_ADMIN"})
    @Test
    void getAllUserRsWithAdminRoleAuthSuccess() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/users"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("Admin1"))
                .andExpect(jsonPath("$.data[1].name").value("User1"))
                .andExpect(jsonPath("$.data[2].name").value("User2"))
                .andExpect(jsonPath("$.data[3].name").value("Admin2"))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @WithMockUser(username = "1", authorities = {"ROLE_USER"})
    @Test
    void getAllUserRsWithUserRoleAuthFail() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/users"))
                .andDo(print())
                .andExpectAll(status().isForbidden());
    }

    @WithAnonymousUser
    @Test
    void getAllUserRsWithAnonymousUserAuthFail() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/users"))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @WithMockUser(authorities = {"ROLE_ADMIN"})
    @Test
    void getUserRsByIdWithAdminRoleAuthSuccess() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/users/1"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Admin1"))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @WithMockUser(authorities = {"ROLE_ADMIN"})
    @Test
    void getUserRsByIdWithAdminRoleAuthWithIsNotPresentUserIdFail() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/users/100"))
                .andDo(print())
                .andExpectAll(status().isNotFound());
    }

    @WithMockUser(authorities = {"ROLE_USER"})
    @Test
    void getUserRsByIdWithUserRoleAuthFail() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/users/1"))
                .andDo(print())
                .andExpectAll(status().isForbidden());
    }

    @WithAnonymousUser
    @Test
    void getUserRsByIdWithAnonymousUserAuthFail() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/users/1"))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @WithMockUser(authorities = {"ROLE_ADMIN"})
    @Test
    void addNewRoleByIdWithAdminRoleAuthSuccess() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/users/4/role/ROLE_USER"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data").value("Пользователю с id 4 добавлена новая роль - ROLE_USER"))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @WithMockUser(authorities = {"ROLE_ADMIN"})
    @Test
    void addNewRoleByIdWithIsNotPresentUserIdFail() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/users/100/role/ROLE_USER"))
                .andDo(print())
                .andExpectAll(status().isNotFound());
    }

    @WithMockUser(authorities = {"ROLE_ADMIN"})
    @Test
    void addNewRoleByIdWithIsNotPresentROLEFail() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/users/4/role/ROLE_MANAGER"))
                .andDo(print())
                .andExpectAll(status().isNotFound());
    }

    @WithMockUser(authorities = {"ROLE_USER"})
    @Test
    void addNewRoleByIdWithUserRoleAuthFail() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/users/4/role/ROLE_USER"))
                .andDo(print())
                .andExpectAll(status().isForbidden());
    }

    @WithAnonymousUser
    @Test
    void addNewRoleByIdWithAnonymousUserAuthFail() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/users/4/role/ROLE_USER"))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @WithMockUser(authorities = {"ROLE_ADMIN"})
    @Test
    void deleteUserWithAdminRoleAuthSuccess() throws Exception {
        this.mockMvc.perform(delete("http://localhost:" + port + "/api/v1/users/1"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data").value("User with id 1 delete."))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @WithMockUser(authorities = {"ROLE_ADMIN"})
    @Test
    void deleteUserWithAdminRoleAuthAndIsNotPresentUserIdFail() throws Exception {
        this.mockMvc.perform(delete("http://localhost:" + port + "/api/v1/users/100"))
                .andDo(print())
                .andExpectAll(status().isNotFound());
    }

    @WithMockUser(authorities = {"ROLE_USER"})
    @Test
    void deleteUserWithUserRoleAuthFail() throws Exception {
        this.mockMvc.perform(delete("http://localhost:" + port + "/api/v1/users/1"))
                .andDo(print())
                .andExpectAll(status().isForbidden());
    }

    @WithAnonymousUser
    @Test
    void deleteUserWithAnonymousUserAuthFail() throws Exception {
        this.mockMvc.perform(delete("http://localhost:" + port + "/api/v1/users/1"))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @WithMockUser(username = "User1", authorities = {"ROLE_USER"})
    @WithUserDetails("User1")
    @Test
    void helloUserWithUserRoleAuthSuccess() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/users/hello"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data").value("Hello user, User1!"))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @WithMockUser(authorities = {"ROLE_ADMIN"})
    @Test
    void helloUserWithAdminRoleAuthFail() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/users/hello"))
                .andDo(print())
                .andExpectAll(status().isForbidden());
    }

    @WithAnonymousUser
    @Test
    void helloUserWithAnonymousUserAuthFail() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/users/hello"))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }
}