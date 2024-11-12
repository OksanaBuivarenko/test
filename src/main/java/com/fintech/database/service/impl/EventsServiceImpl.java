package com.fintech.database.service.impl;

import com.fintech.database.dto.kudago.EventsDto;
import com.fintech.database.dto.request.EventsFilterRq;
import com.fintech.database.dto.request.EventsRq;
import com.fintech.database.dto.response.EventsRs;
import com.fintech.database.dto.response.PageRs;
import com.fintech.database.entity.Events;
import com.fintech.database.entity.Events_;
import com.fintech.database.entity.Locations;
import com.fintech.database.entity.Locations_;
import com.fintech.database.exception.ObjectNotFoundException;
import com.fintech.database.exception.RelatedEntityNotFound;
import com.fintech.database.mapper.EventsMapper;
import com.fintech.database.repository.EventsRepository;
import com.fintech.database.repository.Specs;
import com.fintech.database.service.EventsService;
import com.fintech.database.service.LocationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventsServiceImpl implements EventsService {

    private final EventsRepository eventsRepository;

    private final LocationsService locationsService;

    private final EventsMapper eventsMapper;

    @Override
    public PageRs<List<EventsRs>> getFilterEventsRs(EventsFilterRq eventsFilterRq) {
        List<Events> list = eventsRepository.findAll(
                Specification.where(Specs.like(Events_.name, eventsFilterRq.getName()))
                        .and(Specs.eq(Events_.locations, Locations_.name, eventsFilterRq.getLocations()))
                        .and(Specs.greaterEq(Events_.dates, eventsFilterRq.getFromDate()))
                        .and(Specs.lessEq(Events_.dates, eventsFilterRq.getToDate())),
                Sort.by(Sort.Direction.ASC, "dates"));
        return PageRs.<List<EventsRs>>builder()
                .data(list.stream().map(eventsMapper::toDto).collect(Collectors.toList()))
                .build();
    }

    @Override
    public PageRs<List<EventsRs>> getAllEventsRs() {
        return PageRs.<List<EventsRs>>builder()
                .data(eventsRepository.findAll().stream().map(eventsMapper::toDto).collect(Collectors.toList()))
                .build();
    }

    public PageRs<EventsRs> getEventsRsById(Long id) {
        return PageRs.<EventsRs>builder()
                .data(eventsMapper.toDto(getEventsById(id)))
                .build();
    }

    public Events getEventsById(Long id) {
        return eventsRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Events", id));
    }

    @Override
    public PageRs<EventsRs> createEvents(EventsRq eventsRq) {
        Locations locations;
        try {
            locations = locationsService.getLocationsByName(eventsRq.getLocations());
        } catch (Exception e) {
            throw new RelatedEntityNotFound("Related entity location with name " + eventsRq.getLocations() + " not found");
        }
        Events events = eventsMapper.toEntity(eventsRq, locations);
        return PageRs.<EventsRs>builder()
                .data(eventsMapper.toDto(eventsRepository.save(events)))
                .build();
    }

    @Override
    public PageRs<EventsRs> updateEvents(Long id, EventsRq eventsRq) {
        Events events = getEventsById(id);
        if (eventsRq.getName() != null && !eventsRq.getName().trim().isEmpty()) {
            events.setName(eventsRq.getName());
        }
        if (eventsRq.getDates() != null) {
            events.setDates(eventsRq.getDates());
        }
        if (eventsRq.getPrice() != null && !eventsRq.getPrice().trim().isEmpty()) {
            events.setPrice(eventsRq.getPrice());
        }
        if (eventsRq.getLocations() != null && !eventsRq.getLocations().trim().isEmpty()) {
            Locations locations;
            try {
                locations = locationsService.getLocationsByName(eventsRq.getLocations());
            } catch (Exception e) {
                throw new RelatedEntityNotFound("Related entity location with name " + eventsRq.getLocations() + " not found");
            }
            events.setLocations(locations);
        }
        return PageRs.<EventsRs>builder()
                .data(eventsMapper.toDto(eventsRepository.save(events)))
                .build();
    }

    @Override
    public PageRs<String> deleteEvents(Long id) {
        Events events = getEventsById(id);
        eventsRepository.delete(events);
        return PageRs.<String>builder()
                .data("Events with id " + id + " delete.")
                .build();
    }

    @Override
    public List<Events> getEventsListFromEventsDtoList(List<EventsDto> dtoList) {
        return dtoList.stream().map(dto -> {
                    Locations locations = locationsService.getLocationsByNameFromDto(dto.getLocation());
                    return eventsMapper.toEntityFromKudagoDto(dto, locations);
                }
        ).collect(Collectors.toList());
    }

    @Override
    public void saveEventsList(List<Events> eventsList) {
        eventsRepository.saveAll(eventsList);
    }
}