package com.fintech.database.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LocationsRs {

    @Schema(example = "4567")
    private Long id;

    @Schema(example = "msk")
    private String slug;

    @Schema(example = "Moscow")
    private String name;
}