package com.fintech.database.service.impl;

import com.fintech.database.service.DataInitializer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataInitializerImpl implements DataInitializer {

    private final UserRoleServiceImpl userRoleService;

    private final UserServiceImpl userService;

    @Override
    public void init() {
        userRoleService.createRole("ROLE_USER");
        userRoleService.createRole("ROLE_ADMIN");

        userService.createDefaultAdmin();
    }
}