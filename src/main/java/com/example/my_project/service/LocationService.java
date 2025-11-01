package com.example.my_project.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.my_project.entity.LocationEntity;
import com.example.my_project.mapper.LocationMapper;
import com.example.my_project.model.Location;
import com.example.my_project.repository.LocationRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LocationService {

    public final LocationRepository locationRepository;
    public final LocationMapper locationMapper;
    private static final Logger log = LoggerFactory.getLogger(LocationService.class);

    public LocationService(
            LocationRepository locationRepository,
            LocationMapper locationMapper) {
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
    }

    @Transactional(readOnly = true)
    public List<Location> getAllLocation() {
        log.info("Getting all locations");
        List<LocationEntity> ListLocationEntity = locationRepository.findAll();
        return ListLocationEntity.stream()
                .map(locationMapper::toModel)
                .toList();
    }

    @Transactional
    public Location createLocation(Location location) {
        log.info("Creating new location with name: {}", location.getName());
        LocationEntity locationEntity = locationRepository.save(locationMapper.toEntity(location));
        log.info("Location created successfully with id: {}", locationEntity.getId());
        return locationMapper.toModel(locationEntity);
    }

    @Transactional(readOnly = true)
    public Location getLocationById(Long LocationId) {
        log.info("Getting location by id: {}", LocationId);
        LocationEntity locationEntity = locationRepository.findById(LocationId)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with id: " + LocationId));
        log.info("Location found with id: {}", LocationId);
        return locationMapper.toModel(locationEntity);
    }

    @Transactional
    public void deleteLocationById(Long id) {
        log.info("Deleting location with id: {}", id);
        locationRepository.deleteById(id);
        log.info("Location deleted successfully with id: {}", id);
    }

    @Transactional
    public Location updateLocation(Long id, Location location) {
        log.info("Updating location with id: {}", id);
        LocationEntity locationEntity = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with id: " + id));
        log.debug("Updating location fields - name: {}, address: {}, capacity: {}",
                location.getName(), location.getAddress(), location.getCapacity());
        locationEntity.setName(location.getName());
        locationEntity.setAddress(location.getAddress());
        locationEntity.setCapacity(location.getCapacity());
        locationEntity.setDescription(location.getDescription());
        locationRepository.save(locationEntity);
        log.info("Location updated successfully with id: {}", id);
        return locationMapper.toModel(locationEntity);
    }

}