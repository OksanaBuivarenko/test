package com.fintech.database.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Builder
@Data
public class ErrorRsWithParam {
    @Schema(example = "UNAUTHORIZED")
    private HttpStatus status;

    @Schema(example = "Bad credentials")
    private String error;

    @Schema(example = "email: must be a well-formed email address")
    private Map<String, String> params;
}