package com.fintech.database.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageRs<T> {

    @Builder.Default
    @Schema(description = "Current time", example = "11-10-2024 13:51:37")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dateTime = LocalDateTime.now();

    @Schema(example = "Collection of objects or just object any type")
    private T data;
}