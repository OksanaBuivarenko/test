package com.fintech.database.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fintech.database.TestContainers;
import com.fintech.database.dto.request.ChangePasswordRq;
import com.fintech.database.dto.request.CreateUserRq;
import com.fintech.database.dto.request.LoginRq;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(scripts = "/sql/insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestContainers.class)
@AutoConfigureMockMvc
class AuthControllerTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void authUserSuccess() throws Exception {
        LoginRq loginRq = LoginRq.builder()
                .username("User1")
                .password("F1^23ghw&")
                .rememberMe(false)
                .build();

        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginRq)))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data.username").value("User1"))
                .andExpect(jsonPath("$.data.email").value("user1@mail.ru"))
                .andExpect(jsonPath("$.data.roles[0]").value("ROLE_USER"))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void authUserWithNotCorrectUsernameFail() throws Exception {
        LoginRq loginRq = LoginRq.builder()
                .username("User11111")
                .password("F1^23ghw&")
                .rememberMe(false)
                .build();

        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginRq)))
                .andDo(print())
                .andExpectAll(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Bad credentials"));
    }

    @Test
    void authUserWithNotCorrectPasswordFail() throws Exception {
        LoginRq loginRq = LoginRq.builder()
                .username("User1")
                .password("111")
                .rememberMe(false)
                .build();

        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginRq)))
                .andDo(print())
                .andExpectAll(status().isBadRequest())
                .andExpect(jsonPath("$.params.password").value("Неверный формат пароля. " +
                        "Пароль должен состоять из букв, цифр и символов, обязательно содержать заглавную латинскую" +
                        " букву, цифру и иметь длину не менее 8 символов"))
                .andExpect(jsonPath("$.error").value("Not valid fields"));
    }

    @Test
    void registerUserSuccess() throws Exception {
        CreateUserRq createUserRq = CreateUserRq.builder()
                .username("NewUser")
                .email("newuser@mail.ru")
                .password("O1^23ghw&")
                .build();

        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createUserRq)))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data").value("User create successfully!"));
    }

    @Test
    void registerUserWithNotCorrectEmailFail() throws Exception {
        CreateUserRq createUserRq = CreateUserRq.builder()
                .username("NewUser")
                .email("newuser")
                .password("O1^23ghw&")
                .build();

        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createUserRq)))
                .andDo(print())
                .andExpectAll(status().isBadRequest())
                .andExpect(jsonPath("$.params.email").value("must be a well-formed email address"))
                .andExpect(jsonPath("$.error").value("Not valid fields"));
    }

    @Test
    void registerUserWithNotCorrectPasswordFail() throws Exception {
        CreateUserRq createUserRq = CreateUserRq.builder()
                .username("NewUser")
                .email("newuser")
                .password("O111")
                .build();

        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createUserRq)))
                .andDo(print())
                .andExpectAll(status().isBadRequest())
                .andExpect(jsonPath("$.params.password").value("Неверный формат пароля. " +
                        "Пароль должен состоять из букв, цифр и символов, обязательно содержать заглавную латинскую" +
                        " букву, цифру и иметь длину не менее 8 символов"))
                .andExpect(jsonPath("$.error").value("Not valid fields"));
    }

    @WithMockUser
    @Test
    void getConfirmationCodeForChangePasswordByEmailSuccess() throws Exception {
        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/auth/change-password/code"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data").value("Confirmation code for change password send " +
                        "to your email!"));
    }

    @WithAnonymousUser
    @Test
    void getConfirmationCodeForChangePasswordByEmailWithAnonymousUserFail() throws Exception {
        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/auth/change-password/code"))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @WithUserDetails("User2")
    @WithMockUser
    @Test
    void changePasswordByEmailWithCorrectCodeSuccess() throws Exception {
        ChangePasswordRq changePasswordRq = ChangePasswordRq.builder().code("0000").newPassword("O5^55ghw&").build();

        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/auth/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(changePasswordRq)))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data").value("Password changed successfully!"));
    }

    @WithMockUser
    @Test
    void changePasswordByEmailWithNotCorrectCodeFail() throws Exception {
        ChangePasswordRq changePasswordRq = ChangePasswordRq.builder().code("5555").newPassword("O5^55ghw&").build();

        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/auth/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(changePasswordRq)))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data").value("Code is not valid!"));
    }

    @WithAnonymousUser
    @Test
    void changePasswordByEmailWithAnonymousUserFail() throws Exception {
        ChangePasswordRq changePasswordRq = ChangePasswordRq.builder().code("0000").newPassword("O5^55ghw&").build();

        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/auth/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(changePasswordRq)))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @WithMockUser
    @WithUserDetails("User1")
    @Test
    void logoutSuccess() throws Exception {

        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/auth/logout"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data").value("User logged out!"));
    }

    @WithAnonymousUser
    @Test
    void logoutWithAnonymousUserFail() throws Exception {

        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/auth/logout"))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }
}