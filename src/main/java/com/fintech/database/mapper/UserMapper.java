package com.fintech.database.mapper;

import com.fintech.database.dto.response.UserRs;
import com.fintech.database.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserRs toDto(User user);
}