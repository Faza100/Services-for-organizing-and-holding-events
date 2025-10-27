package com.example.my_project.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.my_project.dto.location.LocationCreateRequestDto;
import com.example.my_project.dto.location.LocationResponseDto;
import com.example.my_project.dto.location.LocationUpdateRequestDto;
import com.example.my_project.mapper.LocationMapper;
import com.example.my_project.model.Location;
import com.example.my_project.service.LocationService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/locations")
@SecurityRequirement(name = "JWT")
public class LocationController {

    private final LocationService locationService;
    private final LocationMapper locationMapper;
    private static final Logger log = LoggerFactory.getLogger(LocationController.class);

    public LocationController(
            LocationService locationService,
            LocationMapper locationMapper) {
        this.locationService = locationService;
        this.locationMapper = locationMapper;
    }

    @GetMapping
    public ResponseEntity<List<LocationResponseDto>> getAllLocation() {
        List<Location> locations = locationService.getAllLocation();
        List<LocationResponseDto> locationDtos = locations.stream()
                .map(locationMapper::toDto)
                .toList();
        log.info("Location get list successfully: {}", locationDtos);
        return ResponseEntity.ok(locationDtos);
    }

    @PostMapping
    public ResponseEntity<LocationResponseDto> createLocation(
            @RequestBody @Valid LocationCreateRequestDto locationCreateRequestDto) {
        log.info("Location create request: {}", locationCreateRequestDto);
        Location locations = locationService.createLocation(
                locationMapper.toModel(locationCreateRequestDto));
        LocationResponseDto response = locationMapper.toDto(locations);
        log.info("Location create successfully: {}", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<LocationResponseDto> getLocationById(
            @PathVariable Long locationId) {
        log.info("Request to get location by id: {}", locationId);
        Location locations = locationService.getLocationById(locationId);
        log.info("Location get by id successfully: {}", locationId);
        return ResponseEntity.ok(locationMapper.toDto(locations));
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<Void> deleteLocationById(
            @PathVariable Long locationId) {
        log.info("Request to delete location by id: {}", locationId);
        locationService.deleteLocationById(locationId);
        log.info("Location delete by id successfully: {}", locationId);
        return ResponseEntity.status(204).build();
    }

    @PutMapping("/{locationId}")
    public ResponseEntity<LocationResponseDto> updateLocation(
            @PathVariable Long locationId,
            @RequestBody @Valid LocationUpdateRequestDto locationUpdateRequestDto) {
        log.info("Received update location request for id {}: {}", locationId, locationUpdateRequestDto);
        Location locations = locationService.updateLocation(locationId,
                locationMapper.toModel(locationUpdateRequestDto));
        LocationResponseDto response = locationMapper.toDto(locations);
        log.info("Location update successfully: {}", response);
        return ResponseEntity.ok(response);
    }
}