package com.fintech.database.service.impl;

import com.fintech.database.dto.kudago.EventsDto;
import com.fintech.database.dto.response.PageRs;
import com.fintech.database.entity.Events;
import com.fintech.database.service.EventsService;
import com.fintech.database.service.FillDbService;
import com.fintech.database.service.HttpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FillDbServiceImpl implements FillDbService {

    private final HttpService<EventsDto> httpService;

    private final EventsService eventsService;

    @Override
    public PageRs<String> fillDb() {
        List<EventsDto> eventsDtoList = httpService.getListByApi();
        List<Events> eventsList = eventsService.getEventsListFromEventsDtoList(eventsDtoList);
        eventsService.saveEventsList(eventsList);
        log.info("Database is filling.");
        return PageRs.<String>builder()
                .data("Database is filling.")
                .build();
    }
}