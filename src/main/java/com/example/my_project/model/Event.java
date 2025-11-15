package com.example.my_project.model;

import java.time.LocalDateTime;

import com.example.my_project.enums.EventStatus;

public class Event {

    private Integer occupiedPlaces = 0;

    private LocalDateTime date;

    private Integer duration;

    private Integer cost;

    private Integer maxPlaces;

    private Long locationId;

    private String name;

    private Long id;

    private Long ownerId;

    private EventStatus status = EventStatus.PLANNED;

    public Event(Integer occupiedPlaces,
            LocalDateTime date,
            Integer duration,
            Integer cost,
            Integer maxPlaces,
            Long locationId,
            String name,
            Long id,
            Long ownerId,
            EventStatus status) {
        this.occupiedPlaces = occupiedPlaces;
        this.date = date;
        this.duration = duration;
        this.cost = cost;
        this.maxPlaces = maxPlaces;
        this.locationId = locationId;
        this.name = name;
        this.id = id;
        this.ownerId = ownerId;
        this.status = status;

    }

    public Event(
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

    public Integer getOccupiedPlaces() {
        return occupiedPlaces;
    }

    public void setOccupiedPlaces(Integer occupiedPlaces) {
        this.occupiedPlaces = occupiedPlaces;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }
}
