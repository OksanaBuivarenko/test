package com.fintech.database.controller;

import com.fintech.database.dto.request.LocationRq;
import com.fintech.database.dto.response.LocationsRs;
import com.fintech.database.dto.response.PageRs;
import com.fintech.database.exception.ObjectAlreadyExistsException;
import com.fintech.database.service.LocationsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@SecurityRequirement(name = "Bearer Authentication")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/locations")
public class LocationsController {

    private final LocationsService locationsService;

    @Operation(summary = "Получение всех городов")
    @GetMapping()
    public PageRs<List<LocationsRs>> getLocations() {
        return locationsService.getLocationsRs();
    }

    @Operation(summary = "Получение города по идентификатору")
    @GetMapping("/{id}")
    public PageRs<LocationsRs> getLocationById(@PathVariable Long id) {
        return locationsService.getLocationRsById(id);
    }

    @Operation(summary = "Создание города")
    @PostMapping()
    public PageRs<LocationsRs> createLocation(@Valid @RequestBody LocationRq locationRq)
            throws ObjectAlreadyExistsException {
        return locationsService.createLocation(locationRq);
    }

    @Operation(summary = "Изменение города")
    @PutMapping("/{id}")
    public PageRs<LocationsRs> updateLocation(@PathVariable Long id, @Valid @RequestBody LocationRq locationRq) {
        return locationsService.updateLocation(id, locationRq);
    }

    @Operation(summary = "Удаление города")
    @DeleteMapping("/{id}")
    public PageRs<String> deleteLocation(@PathVariable Long id) {
        return locationsService.deleteLocation(id);
    }
}