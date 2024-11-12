package com.fintech.database.controller;

import com.fintech.database.dto.response.PageRs;
import com.fintech.database.dto.response.UserRs;
import com.fintech.database.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "Bearer Authentication")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Получение всех пользователей")
    @GetMapping()
    public PageRs<List<UserRs>> getAllUserRs() {
        return userService.getAllUserRs();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Получение пользователя по идентификатору")
    @GetMapping("/{id}")
    public PageRs<UserRs> getUserRsById(@PathVariable Long id) {
        return userService.getUserRsById(id);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Добавление пользователю новой роли по идентификатору")
    @GetMapping("/{id}/role/{role}")
    public PageRs<String> addNewRoleById(@PathVariable Long id, @PathVariable String role) {
        return userService.addNewRoleById(id, role);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Удаление пользователя")
    @DeleteMapping("/{id}")
    public PageRs<String> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "Приветственное сообщение для user")
    @GetMapping("/hello")
    public PageRs<String> helloUser() {
        return userService.helloUser();
    }
}