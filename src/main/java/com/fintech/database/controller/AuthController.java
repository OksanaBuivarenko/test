package com.fintech.database.controller;

import com.fintech.database.dto.request.ChangePasswordRq;
import com.fintech.database.dto.request.CreateUserRq;
import com.fintech.database.dto.request.LoginRq;
import com.fintech.database.dto.response.AuthRs;
import com.fintech.database.dto.response.PageRs;
import com.fintech.database.service.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final SecurityService securityService;

    @Operation(summary = "Вход пользователя по логину и паролю")
    @PostMapping("/signin")
    public PageRs<AuthRs> authUser(@RequestBody @Valid LoginRq loginRq) {
        return securityService.authenticate(loginRq);
    }

    @Operation(summary = "Регистрация нового пользователя")
    @PostMapping("/signup")
    public PageRs<String> registerUser(@RequestBody @Valid CreateUserRq createUserRq) {
        return securityService.register(createUserRq);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Запрос на изменение пароля c подтверждением по электронной почте")
    @PostMapping("change-password/code")
    public PageRs<String> getConfirmationCodeForChangePasswordByEmail() {
        return securityService.getConfirmationCodeForChangePasswordByEmail();
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Запрос на изменение пароля c подтверждением по электронной почте")
    @PostMapping("change-password")
    public PageRs<String> changePasswordByEmail(@RequestBody @Valid ChangePasswordRq changePasswordRq) {
        return securityService.changePasswordByEmail(changePasswordRq);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Выход пользователя из системы")
    @PostMapping("/logout")
    public PageRs<String> logoutUser() {
        return securityService.logout();
    }
}