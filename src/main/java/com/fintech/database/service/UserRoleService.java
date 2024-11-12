package com.fintech.database.service;

import com.fintech.database.dto.request.UserRoleRq;
import com.fintech.database.dto.response.PageRs;
import com.fintech.database.dto.response.RoleRs;
import com.fintech.database.entity.UserRole;

import java.util.List;

public interface UserRoleService {

    UserRole getRole(String name);

    UserRole createRole(String name);

    PageRs<List<RoleRs>> getAllRolesRs();

    PageRs<RoleRs> getRolesRsById(Long id);

    PageRs<RoleRs> updateRole(Long id, UserRoleRq userRoleRq);

    PageRs<String> deleteRole(Long id);

    PageRs<RoleRs> createRoleRs(UserRoleRq userRoleRq);
}