package com.example.my_project.service.event;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.my_project.entity.EventEntity;
import com.example.my_project.entity.LocationEntity;
import com.example.my_project.entity.UserEntity;
import com.example.my_project.enums.EventStatus;
import com.example.my_project.enums.UserRole;
import com.example.my_project.exeption.NoRolesException;
import com.example.my_project.mapper.EventMapper;
import com.example.my_project.model.Event;
import com.example.my_project.model.EventFilter;
import com.example.my_project.repository.EventRepository;
import com.example.my_project.repository.LocationRepository;
import com.example.my_project.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final SecurityContextService securityContextService;
    private static final Logger log = LoggerFactory.getLogger(EventService.class);

    public EventService(EventRepository eventRepository,
            EventMapper eventMapper,
            UserRepository userRepository,
            LocationRepository locationRepository,
            SecurityContextService securityContextService) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.securityContextService = securityContextService;
    }

    @Transactional
    public Event createEvent(Event event) {
        log.info("Creating new event with name: {}", event.getName());

        EventEntity eventEntity = eventRepository.save(prepareEventEntity(event));

        log.info("Event created successfully with id: {}", event.getId());
        return eventMapper.toModel(eventEntity);
    }

    @Transactional
    public Event getEventById(Long eventId) {
        log.info("Getting event by id: {}", eventId);

        EventEntity eventEntity = findByEventId(eventId);

        log.info("Event found with id: {}", eventId);
        eventEntity.updateStatus();

        return eventMapper.toModel(eventEntity);
    }

    @Transactional
    public void cancelEventById(Long eventId) {
        log.info("Cancelling event with id: {}", eventId);

        EventEntity cancelledEvent = validateAndCancelEvent(eventId);
        eventRepository.save(cancelledEvent);

        log.info("Event successfully cancelled with id: {}", eventId);
    }

    @Transactional
    public Event updateEvent(Long eventId, Event event) {
        log.info("Updating event with id: {}", eventId);

        EventEntity eventEntity = findByEventId(eventId);

        LocationEntity location = findByLocationId(event.getLocationId());
        validateCapacity(location, event.getMaxPlaces());

        eventEntity.setDate(event.getDate());
        eventEntity.setDuration(event.getDuration());
        eventEntity.setCost(event.getCost());
        eventEntity.setMaxPlaces(event.getMaxPlaces());
        eventEntity.setLocation(location);
        eventEntity.setName(event.getName());

        log.info("Event updated successfully with id: {}", eventId);
        eventRepository.save(eventEntity);
        return eventMapper.toModel(eventEntity);
    }

    @Transactional(readOnly = true)
    public List<Event> getMyEvents() {

        log.info("Getting user created events");
        String login = securityContextService.getCurrentUserLogin();

        UserEntity userEntity = findByUserLogin(login);

        log.info("successful receipt of user-created events");
        return userEntity.getCreatedEvents().stream()
                .map(eventMapper::toModel)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Event> searchEvents(EventFilter filter) {
        log.info("Searching events with filter: {}", filter);

        List<EventEntity> events = eventRepository.findEvents(
                filter.getName(),
                filter.getPlacesMin(), // ← теперь на 2-м месте
                filter.getPlacesMax(), // ← теперь на 3-м месте
                filter.getDateStartAfter(), // ← теперь на 4-м месте
                filter.getDateStartBefore(), // ← теперь на 5-м месте
                filter.getCostMin(), // ← теперь на 6-м месте
                filter.getCostMax(), // ← теперь на 7-м месте
                filter.getDurationMin(), // ← теперь на 8-м месте
                filter.getDurationMax(), // ← теперь на 9-м месте
                filter.getLocationId(), // ← теперь на 10-м месте
                filter.getEventStatus());

        log.info("Found {} events", events.size());
        return events.stream()
                .map(eventMapper::toModel)
                .toList();
    }

    private EventEntity prepareEventEntity(Event event) {
        validateEventCreation(event);
        UserEntity userEntity = getCurrentUser();
        LocationEntity locationEntity = getValidatedLocation(event.getLocationId(), event);

        return eventMapper.toEntity(event, locationEntity, userEntity);
    }

    private void validateEventCreation(Event event) {
        if (event.getDuration() < 1) {
            throw new IllegalArgumentException("Event duration must be at least 1 minute");
        }
    }

    private UserEntity getCurrentUser() {
        String login = securityContextService.getCurrentUserLogin();
        return findByUserLogin(login);
    }

    private LocationEntity getValidatedLocation(Long locationId, Event event) {
        LocationEntity locationEntity = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with id: " + locationId));

        validateCapacity(locationEntity, event.getMaxPlaces());
        updateEventsStatusForLocation(locationEntity);

        return locationEntity;
    }

    private void validateCapacity(LocationEntity location, Integer eventMaxPlaces) {
        if (location.getCapacity() < eventMaxPlaces) {
            throw new IllegalArgumentException(
                    String.format("Event capacity %d exceeds location capacity %d",
                            eventMaxPlaces, location.getCapacity()));
        }
    }

    private void updateEventsStatusForLocation(LocationEntity location) {
        List<EventEntity> events = location.getEvents();
        for (EventEntity event : events) {
            event.updateStatus();
        }
        eventRepository.saveAll(events);
    }

    private EventEntity findByEventId(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + eventId));
    }

    private UserEntity findByUserLogin(String login) {
        return userRepository.getUserByLogin(login)
                .orElseThrow(() -> new EntityNotFoundException("User not found with login: " + login));
    }

    private LocationEntity findByLocationId(Long locationId) {
        return locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with id: "
                        + locationId));

    }

    private EventEntity validateAndCancelEvent(Long eventId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new NoRolesException("User not authenticated");
        }

        UserRole role = securityContextService.getCurrentUserRole();

        String login = authentication.getName();

        EventEntity eventEntity = findByEventId(eventId);

        if (role.equals(UserRole.ADMIN)) {
            eventEntity.setStatus(EventStatus.CANCELLED);
            log.info("Event cancelled by ADMIN with id: {}", eventId);
            return eventEntity;
        }

        if (role.equals(UserRole.USER)) {
            UserEntity userEntity = findByUserLogin(login);

            if (eventEntity.getOwner().getId().equals(userEntity.getId())) {
                eventEntity.setStatus(EventStatus.CANCELLED);
                log.info("Event cancelled by owner with id: {}", eventId);
                return eventEntity;
            } else {
                throw new SecurityException("You can only cancel your own events");
            }
        }
        throw new SecurityException("Insufficient permissions to cancel event");
    }
}
