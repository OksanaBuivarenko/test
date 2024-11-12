package com.fintech.database.mapper;

import com.fintech.database.dto.request.UserRoleRq;
import com.fintech.database.dto.response.RoleRs;
import com.fintech.database.entity.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper {

    RoleRs toDto(UserRole userRole);

    UserRole toEntity(UserRoleRq userRoleRq);
}