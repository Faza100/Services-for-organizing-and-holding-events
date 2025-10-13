package com.example.my_progect.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.my_progect.entity.LocationEntity;
import com.example.my_progect.mapper.LocationMapper;
import com.example.my_progect.model.Location;
import com.example.my_progect.repository.LocationRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class LocationService {

    public final LocationRepository locationRepository;
    public final LocationMapper locationMapper;

    public LocationService(
            LocationRepository locationRepository,
            LocationMapper locationMapper) {
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
    }

    @Transactional
    public List<Location> getAllLocation() {
        List<LocationEntity> ListLocationEntity = locationRepository.findAll();
        return ListLocationEntity.stream()
                .map(locationMapper::toModel)
                .toList();
    }

    @Transactional
    public Location createLocation(Location location) {
        LocationEntity locationEntity = locationRepository.save(locationMapper.toEntity(location));
        return locationMapper.toModel(locationEntity);
    }

    @Transactional
    public Location getLocationById(Long LocationId) {
        LocationEntity locationEntity = locationRepository.findById(LocationId)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with id: " + LocationId));
        return locationMapper.toModel(locationEntity);
    }

    @Transactional
    public void deleteLocationById(Long id) {
        locationRepository.deleteById(id);
    }

    @Transactional
    public Location updateLocation(Long id, Location location) {
        LocationEntity locationEntity = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with id: " + id));
        locationEntity.setName(location.getName());
        locationEntity.setAddress(location.getAddress());
        locationEntity.setCapacity(location.getCapacity());
        locationEntity.setDescription(location.getDescription());
        locationRepository.save(locationEntity);
        return locationMapper.toModel(locationEntity);
    }

}
