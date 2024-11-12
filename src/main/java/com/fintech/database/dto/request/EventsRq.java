package com.fintech.database.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class EventsRq {

    @NotBlank(groups = Marker.OnCreate.class)
    private String name;

    @NotNull(groups = Marker.OnCreate.class)
    @FutureOrPresent
    private LocalDate dates;

    @NotBlank(groups = Marker.OnCreate.class)
    private String price;

    @NotBlank(groups = Marker.OnCreate.class)
    private String locations;
}