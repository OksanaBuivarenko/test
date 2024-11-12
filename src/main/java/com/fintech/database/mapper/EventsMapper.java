package com.fintech.database.mapper;

import com.fintech.database.dto.kudago.EventsDto;
import com.fintech.database.dto.request.EventsRq;
import com.fintech.database.dto.response.EventsRs;
import com.fintech.database.entity.Events;
import com.fintech.database.entity.Locations;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EventsMapper {

    EventsRs toDto(Events events);

    @Mapping(target = "name", source = "eventsRq.name")
    @Mapping(target = "locations", source = "locations")
    Events toEntity(EventsRq eventsRq, Locations locations);

    @Mapping(target = "name", source = "eventsDto.title")
    @Mapping(target = "dates", source = "eventsDto", qualifiedByName = "dateStart")
    @Mapping(target = "locations", source = "locations")
    @Mapping(target = "id", ignore = true)
    Events toEntityFromKudagoDto(EventsDto eventsDto, Locations locations);

    @Named("dateStart")
    default LocalDate dateStart(EventsDto eventsDto) {
        List<LocalDate> result = eventsDto.getDates().stream()
                .map((dates -> Instant.ofEpochMilli(dates.getStart() * 1000).atZone(ZoneId.systemDefault()).toLocalDate()))
                .filter((date -> date.isAfter(LocalDate.now(ZoneId.systemDefault()))
                        || date.isEqual(LocalDate.now(ZoneId.systemDefault())))).collect(Collectors.toList());
        if (!result.isEmpty()) {
            return result.get(0);
        }
        return Instant.ofEpochMilli(eventsDto.getDates().get(0).getStart() * 1000).atZone(ZoneId.systemDefault()).toLocalDate();
    }
}