package com.example.my_project.dto.event;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.my_project.enums.EventStatus;

import jakarta.validation.constraints.Min;

public class EventFilterDto {

    private Integer durationMax;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dateStartBefore;

    @Min(2)
    private Integer placesMin;

    private Long locationId;

    private EventStatus eventStatus;

    private String name;

    private Integer placesMax;

    @Min(0)
    private Integer costMin;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dateStartAfter;

    private Integer costMax;

    @Min(2)
    private Integer durationMin;

    public EventFilterDto(Integer durationMax,
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
