package com.fintech.database.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EventsFilterRq {

    @Schema(example = "коллекция Эрмитажа «Русская печатная графика»")
    private String name;

    @Schema(example = "Санкт-Петербург")
    private String locations;

    @Schema(example = "2017-03-23")
    @FutureOrPresent
    private LocalDate fromDate;

    @Schema(example = "2017-03-23")
    @FutureOrPresent
    private LocalDate toDate;
}