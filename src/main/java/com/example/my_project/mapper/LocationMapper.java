package com.example.my_project.mapper;

import org.springframework.stereotype.Component;

import com.example.my_project.dto.location.LocationCreateRequestDto;
import com.example.my_project.dto.location.LocationResponseDto;
import com.example.my_project.dto.location.LocationUpdateRequestDto;
import com.example.my_project.entity.LocationEntity;
import com.example.my_project.model.Location;

@Component
public class LocationMapper {

    public Location toModel(LocationCreateRequestDto createRequestDto) {
        return new Location(
                createRequestDto.getName(),
                createRequestDto.getAddress(),
                createRequestDto.getCapacity(),
                createRequestDto.getDescription());
    }

    public Location toModel(LocationEntity locationEntity) {
        return new Location(
                locationEntity.getId(),
                locationEntity.getName(),
                locationEntity.getAddress(),
                locationEntity.getCapacity(),
                locationEntity.getDescription());
    }

    public LocationEntity toEntity(Location location) {
        return new LocationEntity(
                location.getName(),
                location.getAddress(),
                location.getCapacity(),
                location.getDescription());
    }

    public LocationResponseDto toDto(Location location) {
        return new LocationResponseDto(
                location.getId(),
                location.getName(),
                location.getAddress(),
                location.getCapacity(),
                location.getDescription());
    }

    public Location toModel(LocationUpdateRequestDto updateRequestDto) {
        return new Location(
                updateRequestDto.getName(),
                updateRequestDto.getAddress(),
                updateRequestDto.getCapacity(),
                updateRequestDto.getDescription());
    }

}
