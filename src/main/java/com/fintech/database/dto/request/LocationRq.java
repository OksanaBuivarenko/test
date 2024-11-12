package com.fintech.database.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LocationRq {

    @NotBlank
    private String slug;

    @NotBlank
    private String name;
}