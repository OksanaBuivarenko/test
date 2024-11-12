package com.fintech.database.dto.kudago;

import lombok.Data;

import java.util.List;

@Data
public class EventsDto {

    private Long id;

    private String title;

    private List<DatesDto> dates;

    private String price;

    private LocationsDto location;
}
