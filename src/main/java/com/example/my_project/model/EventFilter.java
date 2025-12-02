package com.example.my_project.model;

import java.time.LocalDateTime;

import com.example.my_project.enums.EventStatus;

public class EventFilter {

    private Integer durationMax;
    private LocalDateTime dateStartBefore;
    private Integer placesMin;
    private Long locationId;
    private EventStatus eventStatus;
    private String name;
    private Integer placesMax;
    private Integer costMin;
    private LocalDateTime dateStartAfter;
    private Integer costMax;
    private Integer durationMin;

    public EventFilter(Integer durationMax,
            LocalDateTime dateStartBefore,
            Integer placesMin,
            Long locationId,
            EventStatus eventStatus,
            String name,
            Integer placesMax,
            Integer costMin,
            LocalDateTime dateStartAfter,
            Integer costMax,
            Integer durationMin) {
        this.durationMax = durationMax;
        this.dateStartBefore = dateStartBefore;
        this.placesMin = placesMin;
        this.locationId = locationId;
        this.eventStatus = eventStatus;
        this.name = name;
        this.placesMax = placesMax;
        this.costMin = costMin;
        this.dateStartAfter = dateStartAfter;
        this.costMax = costMax;
        this.durationMin = durationMin;
    }

    public Integer getDurationMax() {
        return durationMax;
    }

    public LocalDateTime getDateStartBefore() {
        return dateStartBefore;
    }

    public Integer getPlacesMin() {
        return placesMin;
    }

    public Long getLocationId() {
        return locationId;
    }

    public EventStatus getEventStatus() {
        return eventStatus;
    }

    public String getName() {
        return name;
    }

    public Integer getPlacesMax() {
        return placesMax;
    }

    public Integer getCostMin() {
        return costMin;
    }

    public LocalDateTime getDateStartAfter() {
        return dateStartAfter;
    }

    public Integer getCostMax() {
        return costMax;
    }

    public Integer getDurationMin() {
        return durationMin;
    }

    public void setDurationMax(Integer durationMax) {
        this.durationMax = durationMax;
    }

    public void setDateStartBefore(LocalDateTime dateStartBefore) {
        this.dateStartBefore = dateStartBefore;
    }

    public void setPlacesMin(Integer placesMin) {
        this.placesMin = placesMin;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public void setEventStatus(EventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlacesMax(Integer placesMax) {
        this.placesMax = placesMax;
    }

    public void setCostMin(Integer costMin) {
        this.costMin = costMin;
    }

    public void setDateStartAfter(LocalDateTime dateStartAfter) {
        this.dateStartAfter = dateStartAfter;
    }

    public void setCostMax(Integer costMax) {
        this.costMax = costMax;
    }

    public void setDurationMin(Integer durationMin) {
        this.durationMin = durationMin;
    }
}
