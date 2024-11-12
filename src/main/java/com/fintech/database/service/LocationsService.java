package com.fintech.database.service;

import com.fintech.database.dto.kudago.LocationsDto;
import com.fintech.database.dto.request.LocationRq;
import com.fintech.database.dto.response.LocationsRs;
import com.fintech.database.dto.response.PageRs;
import com.fintech.database.entity.Locations;
import com.fintech.database.exception.ObjectAlreadyExistsException;

import java.util.List;

public interface LocationsService {

    PageRs<List<LocationsRs>> getLocationsRs();

    PageRs<LocationsRs> getLocationRsById(Long id);

    PageRs<LocationsRs> createLocation(LocationRq locationRq) throws ObjectAlreadyExistsException;

    PageRs<LocationsRs> updateLocation(Long id, LocationRq locationRq);

    PageRs<String> deleteLocation(Long id);

    Locations getLocationsByName(String location);

    Locations getLocationsByNameFromDto(LocationsDto locations);
}