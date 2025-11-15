package com.example.my_project.service.location;

import org.springframework.stereotype.Component;

import com.example.my_project.model.Location;

import jakarta.annotation.PostConstruct;

@Component
public class DefaultLocationInitializer {

    private final LocationService locationService;

    public DefaultLocationInitializer(
            LocationService locationService) {
        this.locationService = locationService;
    }

    @PostConstruct
    public void initLocation() {
        createLocationIfNotExists("Moskow", "St. Moskovskaya, Building 10", 200);
        createLocationIfNotExists("Saint Petersburg", "St. Peterburdskaya, Building 17", 120);
    }

    public void createLocationIfNotExists(
            String name,
            String address,
            Integer capacity) {
        Location location = new Location(
                name,
                address,
                capacity,
                "No description");

        locationService.createLocation(location);

    }

}
