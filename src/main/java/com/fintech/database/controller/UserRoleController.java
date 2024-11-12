package com.fintech.database.controller;

import com.fintech.database.dto.request.UserRoleRq;
import com.fintech.database.dto.response.PageRs;
import com.fintech.database.dto.response.RoleRs;
import com.fintech.database.service.UserRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "Bearer Authentication")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/roles")
public class UserRoleController {

    private final UserRoleService roleService;

    @Operation(summary = "Получение всех ролей")
    @GetMapping()
    public PageRs<List<RoleRs>> getAllRoles() {
        return roleService.getAllRolesRs();
    }

    @Operation(summary = "Получение роли по идентификатору")
    @GetMapping("/{id}")
    public PageRs<RoleRs> getRolesRsById(@PathVariable Long id) {
        return roleService.getRolesRsById(id);
    }


    @Operation(summary = "Создание новой роли")
    @PostMapping()
    public PageRs<RoleRs> createRole(@RequestBody @Valid UserRoleRq userRoleRq) {
        return roleService.createRoleRs(userRoleRq);
    }

    @Operation(summary = "Изменение роли по идентификатору")
    @PutMapping("/{id}")
    public PageRs<RoleRs> updateRole(@PathVariable Long id, @RequestBody @Valid UserRoleRq userRoleRq) {
        return roleService.updateRole(id, userRoleRq);
    }

    @Operation(summary = "Удаление роли")
    @DeleteMapping("/{id}")
    public PageRs<String> deleteRole(@PathVariable Long id) {
        return roleService.deleteRole(id);
    }
}