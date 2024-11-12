package com.fintech.database.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Data
public class EventsRs {

    @Schema(example = "4567")
    private Long id;

    @Schema(example = "2017-03-23")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dates;

    @Schema(example = "выставка Максима Бойкова «Вепсский лес, или История одного дня»")
    private String name;

    @Schema(example = "от 0 до 200 рублей")
    private String price;

    private LocationsRs locations;
}