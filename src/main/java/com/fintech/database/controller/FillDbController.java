package com.fintech.database.controller;

import com.fintech.database.dto.response.PageRs;
import com.fintech.database.service.FillDbService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@SecurityRequirement(name = "Bearer Authentication")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fill/db")
public class FillDbController {

    private final FillDbService fillDbService;

    @Operation(summary = "Заполнение бд данными из сервиса kudago")
    @GetMapping()
    public PageRs<String> getFilterEvents() {
        return fillDbService.fillDb();
    }
}
