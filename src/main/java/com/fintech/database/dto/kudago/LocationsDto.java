package com.fintech.database.dto.kudago;

import lombok.Data;

@Data
public class LocationsDto {

    private String slug;

    private String name;

    private String timezone;

    private Coords coords;

    private String language;

    private String currency;

}
