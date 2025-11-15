package com.example.my_project.dto.event;

import java.time.LocalDateTime;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;

public class EventUpdateRequestDto {
    @FutureOrPresent(message = "Event date cannot be in the past")
    private LocalDateTime date;
    @Min(2)
    private Integer duration;
    @Min(0)
    private Integer cost;
    @Min(2)
    private Integer maxPlaces;

    private Long locationId;

    private String name;

    public EventUpdateRequestDto(
            LocalDateTime date,
            Integer duration,
            Integer cost,
            Integer maxPlaces,
            Long locationId,
            String name) {
        this.date = date;
        this.duration = duration;
        this.cost = cost;
        this.maxPlaces = maxPlaces;
        this.locationId = locationId;
        this.name = name;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getMaxPlaces() {
        return maxPlaces;
    }

    public void setMaxPlaces(Integer maxPlaces) {
        this.maxPlaces = maxPlaces;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
