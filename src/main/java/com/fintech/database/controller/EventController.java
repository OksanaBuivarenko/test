package com.fintech.database.controller;


import com.fintech.database.dto.request.EventsFilterRq;
import com.fintech.database.dto.request.EventsRq;
import com.fintech.database.dto.request.Marker;
import com.fintech.database.dto.response.EventsRs;
import com.fintech.database.dto.response.PageRs;
import com.fintech.database.service.EventsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@SecurityRequirement(name = "Bearer Authentication")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventsService eventsService;

    @Operation(summary = "Получение событий на основе пользовательских пожеланий")
    @GetMapping()
    public PageRs<List<EventsRs>> getFilterEvents(@Valid @ParameterObject EventsFilterRq eventsFilterRq) {
        return eventsService.getFilterEventsRs(eventsFilterRq);
    }

    @Operation(summary = "Получение всех событий")
    @GetMapping("/all")
    public PageRs<List<EventsRs>> getAllEvents() {
        return eventsService.getAllEventsRs();
    }

    @Operation(summary = "Получение события по идентификатору")
    @GetMapping("/{id}")
    public PageRs<EventsRs> getEventsRsById(@PathVariable Long id) {
        return eventsService.getEventsRsById(id);
    }

    @Validated({Marker.OnCreate.class})
    @Operation(summary = "Создать новое событие")
    @PostMapping()
    public PageRs<EventsRs> createEvents(@RequestBody @Valid EventsRq eventsRq) {
        return eventsService.createEvents(eventsRq);
    }

    @Operation(summary = "Изменить событие")
    @PutMapping("/{id}")
    public PageRs<EventsRs> updateEvents(@PathVariable Long id, @RequestBody @Valid EventsRq eventsRq) {
        return eventsService.updateEvents(id, eventsRq);
    }

    @Operation(summary = "Удалить событие")
    @DeleteMapping("/{id}")
    public PageRs<String> deleteEvents(@PathVariable Long id) {
        return eventsService.deleteEvents(id);
    }
}
