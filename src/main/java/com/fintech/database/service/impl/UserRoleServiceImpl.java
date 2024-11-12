package com.fintech.database.service.impl;

import com.fintech.database.dto.request.UserRoleRq;
import com.fintech.database.dto.response.PageRs;
import com.fintech.database.dto.response.RoleRs;
import com.fintech.database.entity.UserRole;
import com.fintech.database.exception.ObjectNotFoundException;
import com.fintech.database.mapper.RoleMapper;
import com.fintech.database.repository.UserRoleRepository;
import com.fintech.database.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository roleRepository;

    private final RoleMapper roleMapper;

    @Override
    public UserRole getRole(String name) {
        return roleRepository.findByName(name).orElseThrow(() -> new ObjectNotFoundException("User role", name));
    }

    public UserRole getRoleById(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("User role", id));
    }

    @Override
    public UserRole createRole(String name) {
        if (!roleRepository.existsByName(name)) {
            UserRole role = new UserRole();
            role.setName(name);
            return roleRepository.save(role);
        }
        return null;
    }

    @Override
    public PageRs<List<RoleRs>> getAllRolesRs() {
        return PageRs.<List<RoleRs>>builder()
                .data(roleRepository.findAll().stream().map(roleMapper::toDto).collect(Collectors.toList()))
                .build();
    }

    @Override
    public PageRs<RoleRs> getRolesRsById(Long id) {
        return PageRs.<RoleRs>builder()
                .data(roleMapper.toDto(getRoleById(id)))
                .build();
    }

    @Override
    public PageRs<RoleRs> updateRole(Long id, UserRoleRq userRoleRq) {
        UserRole role = getRoleById(id);
        role.setName(userRoleRq.getName());
        return PageRs.<RoleRs>builder()
                .data(roleMapper.toDto(roleRepository.save(role)))
                .build();
    }

    @Override
    public PageRs<String> deleteRole(Long id) {
        UserRole role = getRoleById(id);
        roleRepository.delete(role);
        return PageRs.<String>builder()
                .data("User role with id " + id + " delete.")
                .build();
    }

    @Override
    public PageRs<RoleRs> createRoleRs(UserRoleRq userRoleRq) {
        UserRole role = roleMapper.toEntity(userRoleRq);
        return PageRs.<RoleRs>builder()
                .data(roleMapper.toDto(roleRepository.save(role)))
                .build();
    }
}