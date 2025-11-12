package com.example.my_project.service.event;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.my_project.entity.EventEntity;
import com.example.my_project.entity.LocationEntity;
import com.example.my_project.enums.EventStatus;

@Service
public class EventBusinessRulesService {

    private final Integer minEventDuration = 1;
    private final Integer timeMinutes = 30;

    public void enforceDurationConstraint(Integer duration) {
        if (duration < minEventDuration) {
            throw new IllegalArgumentException(
                    "The event must not last less than " + minEventDuration + " minutes");
        }
    }

    public void checkCapacityPolicy(LocationEntity location, Integer eventMaxPlaces) {
        if (location.getCapacity() < eventMaxPlaces) {
            throw new IllegalArgumentException(
                    String.format(
                            "The number of seats at event %d should not exceed the number of seats in the location %d",
                            eventMaxPlaces, location.getCapacity()));
        }
    }

    public boolean isLocationAvailable(List<EventEntity> existingEvents,
            LocalDateTime eventDate,
            Integer eventDuration) {
        LocalDateTime eventStart = eventDate.minusMinutes(timeMinutes);
        LocalDateTime eventEnd = eventDate.plusMinutes(eventDuration);

        return existingEvents.stream()
                .filter(this::isActiveEvent)
                .anyMatch(event -> isTimeOverlap(eventStart, eventEnd,
                        event.getDate(), event.getDate().plusMinutes(event.getDuration())));
    }

    public boolean isActiveEvent(EventEntity event) {
        return !event.getStatus().equals(EventStatus.FINISHED) &&
                !event.getStatus().equals(EventStatus.CANCELLED);
    }

    private boolean isTimeOverlap(LocalDateTime start1, LocalDateTime end1,
            LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && end1.isAfter(start2);
    }
}
