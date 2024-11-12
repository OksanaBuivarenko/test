package com.fintech.database.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserRs {

    @Schema(example = "4567")
    private Long id;

    @Schema(example = "Ivan")
    private String name;

    @Schema(example = "ivan@mail.ru")
    private String email;
}