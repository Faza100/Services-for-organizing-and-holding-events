package com.example.my_progect.converter;

import org.springframework.stereotype.Component;

import com.example.my_progect.dto.location.LocationCreateRequestDto;
import com.example.my_progect.dto.location.LocationResponseDto;
import com.example.my_progect.dto.location.LocationUpdateRequestDto;
import com.example.my_progect.model.Location;

@Component
public class LocationConverter {

    public Location toEntity(LocationCreateRequestDto LocationDto) {
        return new Location(
                LocationDto.getName(),
                LocationDto.getAddress(),
                LocationDto.getCapacity(),
                LocationDto.getDescription());
    }

    public LocationResponseDto toDto(Location location) {
        return new LocationResponseDto(
                location.getId(),
                location.getName(),
                location.getAddress(),
                location.getCapacity(),
                location.getDescription());
    }

    public Location toEntity(LocationUpdateRequestDto LocationDto) {
        return new Location(
                LocationDto.getName(),
                LocationDto.getAddress(),
                LocationDto.getCapacity(),
                LocationDto.getDescription());
    }

}
