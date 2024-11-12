package com.fintech.database.service.impl;

import com.fintech.database.dto.kudago.LocationsDto;
import com.fintech.database.dto.request.LocationRq;
import com.fintech.database.dto.response.LocationsRs;
import com.fintech.database.dto.response.PageRs;
import com.fintech.database.entity.Locations;
import com.fintech.database.exception.ObjectAlreadyExistsException;
import com.fintech.database.exception.ObjectNotFoundException;
import com.fintech.database.mapper.LocationsMapper;
import com.fintech.database.repository.LocationsRepository;
import com.fintech.database.service.LocationsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class LocationsServiceImpl implements LocationsService {

    private final LocationsRepository locationsRepository;

    private final LocationsMapper locationsMapper;

    @Override
    public PageRs<List<LocationsRs>> getLocationsRs() {
        return PageRs.<List<LocationsRs>>builder()
                .data(locationsRepository.findAll().stream().map(locationsMapper::toDto).collect(Collectors.toList()))
                .build();
    }

    @Override
    public PageRs<LocationsRs> getLocationRsById(Long id) {
        return PageRs.<LocationsRs>builder()
                .data(locationsMapper.toDto(getLocationById(id)))
                .build();
    }

    public Locations getLocationById(Long id) {
        return locationsRepository.find(id).orElseThrow(()
                -> new ObjectNotFoundException("Locations", id));
    }

    @Override
    public PageRs<LocationsRs> createLocation(LocationRq locationRq) throws ObjectAlreadyExistsException {
        Locations locations = locationsMapper.toEntity(locationRq);

        if (locationsRepository.findByName(locations.getName()).isPresent()) {
            throw new ObjectAlreadyExistsException("Locations with name " + locations.getName() + " already exists");
        }
        if (locationsRepository.findBySlug(locations.getSlug()).isPresent()) {
            throw new ObjectAlreadyExistsException("Locations with slug " + locations.getSlug() + " already exists");
        }
        return PageRs.<LocationsRs>builder()
                .data(locationsMapper.toDto(locationsRepository.save(locations)))
                .build();
    }

    @Override
    public PageRs<LocationsRs> updateLocation(Long id, LocationRq locationRq) {
        Locations locations = getLocationById(id);
        locations.setName(locationRq.getName());
        locations.setSlug(locationRq.getSlug());
        return PageRs.<LocationsRs>builder()
                .data(locationsMapper.toDto(locationsRepository.save(locations)))
                .build();
    }

    @Override
    public PageRs<String> deleteLocation(Long id) {
        Locations locations = getLocationById(id);
        locationsRepository.delete(locations);
        return PageRs.<String>builder()
                .data("Locations with id " + id + " delete.")
                .build();
    }

    @Override
    public Locations getLocationsByName(String name) {
        return locationsRepository.findByName(name)
                .orElseThrow(() -> new ObjectNotFoundException("Locations", name));
    }

    @Override
    public Locations getLocationsByNameFromDto(LocationsDto locationsDto) {
        Locations locations = locationsMapper.toEntityFromKudagoDto(locationsDto);
        if (locationsRepository.findByName(locations.getName()).isEmpty()) {
            locationsRepository.save(locations);
        }
        return getLocationsByName(locations.getName());
    }
}