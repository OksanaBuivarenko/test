package com.fintech.database.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AuthRs {
    @Schema(example = "4567")
    private Long id;

    private String token;

    @Schema(example = "Ivan123")
    private String username;

    @Schema(example = "email@mail.ru")
    private String email;

    @Schema(example = "roles: [USER, ADMIN]")
    private List<String> roles;
}