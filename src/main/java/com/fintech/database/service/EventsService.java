package com.fintech.database.service;

import com.fintech.database.dto.kudago.EventsDto;
import com.fintech.database.dto.request.EventsFilterRq;
import com.fintech.database.dto.request.EventsRq;
import com.fintech.database.dto.response.EventsRs;
import com.fintech.database.dto.response.PageRs;
import com.fintech.database.entity.Events;

import java.util.List;

public interface EventsService {

    PageRs<List<EventsRs>> getFilterEventsRs(EventsFilterRq eventsFilterRq);

    PageRs<List<EventsRs>> getAllEventsRs();

    PageRs<EventsRs> createEvents(EventsRq eventsRq);

    PageRs<EventsRs> getEventsRsById(Long id);

    PageRs<EventsRs> updateEvents(Long id, EventsRq eventsRq);

    PageRs<String> deleteEvents(Long id);

    List<Events> getEventsListFromEventsDtoList(List<EventsDto> dtoList);

    void saveEventsList(List<Events> eventsList);
}