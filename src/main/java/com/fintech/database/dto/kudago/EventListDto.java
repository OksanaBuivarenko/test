package com.fintech.database.dto.kudago;

import lombok.Data;

import java.util.List;
@Data
public class EventListDto {

    private Long count;

    private String next;

    private String previous;

    private List<EventsDto> results;
}
