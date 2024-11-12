package com.fintech.database.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleRs {

    @Schema(example = "4567")
    private Long id;

    @Schema(example = "ADMIN")
    @NotBlank
    private String name;
}