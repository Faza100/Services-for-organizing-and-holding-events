package com.example.my_progect.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.my_progect.model.Location;
import com.example.my_progect.repository.LocationRepository;

@Service
public class LocationService {

    public final LocationRepository locationRepository;

    public LocationService(
            LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<Location> getAllLocation() {
        return locationRepository.findAll();
    }

    public Location createLocation(Location location) {
        return locationRepository.save(location);
    }

    public Location getUserById(Long LocationId) {
        return locationRepository.findById(LocationId)
                .orElseThrow(() -> new IllegalArgumentException("Location not found with id: " + LocationId));
    }

    public void deleteLocationByid(Long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Location not found with id: " + id));
        locationRepository.delete(location);
    }

    public Location updateLocation(Long id, Location location) {
        Location existingLocation = locationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Location not found with id: " + id));
        if (location.getName() != null) {
            existingLocation.setName(location.getName());
        }
        if (location.getAddress() != null) {
            existingLocation.setAddress(location.getAddress());
        }
        if (location.getCapacity() != null) {
            existingLocation.setCapacity(location.getCapacity());
        }
        if (location.getDescription() != null) {
            existingLocation.setDescription(location.getDescription());
        }
        return locationRepository.save(existingLocation);
    }

}
