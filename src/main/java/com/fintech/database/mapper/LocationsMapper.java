package com.fintech.database.mapper;

import com.fintech.database.dto.kudago.LocationsDto;
import com.fintech.database.dto.request.LocationRq;
import com.fintech.database.dto.response.LocationsRs;
import com.fintech.database.entity.Locations;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LocationsMapper {

    LocationsRs toDto(Locations locations);

    Locations toEntity(LocationRq locationsRq);

    @Mapping(target = "id", ignore = true)
    Locations toEntityFromKudagoDto(LocationsDto locationsDto);
}
