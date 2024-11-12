package com.fintech.database.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Builder
@Data
public class ErrorRs {

    @Schema(example = "UNAUTHORIZED")
    private HttpStatus status;

    @Schema(example = "Bad credentials")
    private String error;
}