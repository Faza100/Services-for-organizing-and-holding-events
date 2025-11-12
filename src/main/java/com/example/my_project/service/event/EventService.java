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
import com.example.my_project.mapper.EventMapper;
import com.example.my_project.model.Event;
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
    private final EventBusinessRulesService eventBusinessRulesService;
    private final SecurityContextService securityContextService;
    private static final Logger log = LoggerFactory.getLogger(EventService.class);

    public EventService(EventRepository eventRepository,
            EventMapper eventMapper,
            UserRepository userRepository,
            LocationRepository locationRepository,
            EventBusinessRulesService eventBusinessRulesService,
            SecurityContextService securityContextService) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.eventBusinessRulesService = eventBusinessRulesService;
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
    public void deleteEventById(Long eventId) {
        log.info("Deleting event with id: {}", eventId);
        cancellationPermission(eventId);
        log.info("Event cancelled");
    }

    @Transactional
    public Event updateEvent(Long eventId, Event event) {
        log.info("Updating location with id: {}", eventId);

        prepareEventEntity(event);

        EventEntity eventEntity = findByEventId(eventId);

        event.setDate(event.getDate());
        event.setDuration(event.getDuration());
        event.setCost(event.getCost());
        event.setMaxPlaces(event.getMaxPlaces());
        event.setLocationId(event.getLocationId());
        event.setName(event.getName());
        log.info("Event updated successfully with id: {}", eventId);
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

    private EventEntity prepareEventEntity(Event event) {
        eventBusinessRulesService.enforceDurationConstraint(event.getDuration());

        String login = securityContextService.getCurrentUserLogin();

        UserEntity userEntity = findByUserLogin(login);

        event.setOwnerId(userEntity.getId());

        LocationEntity locationEntity = locationRepository.findById(event.getLocationId())
                .orElseThrow(() -> new EntityNotFoundException("Location not found with id: "
                        + event.getLocationId()));

        eventBusinessRulesService.checkCapacityPolicy(locationEntity, event.getMaxPlaces());

        updateEventsStatusForLocation(locationEntity);

        if (eventBusinessRulesService.isLocationAvailable(locationEntity.getEvents(), event.getDate(),
                event.getDuration())) {
            throw new IllegalArgumentException("Location is already occupied at this tim");
        }

        return eventMapper.toEntity(event,
                locationEntity, userEntity);
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

    private void cancellationPermission(Long eventId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserRole role = securityContextService.getCurrentUserRole();

        String login = authentication.getName();

        EventEntity eventEntity = findByEventId(eventId);

        if (role.equals(UserRole.ADMIN)) {
            eventEntity.setStatus(EventStatus.CANCELLED);
            log.info("Event cancelled by ADMIN with id: {}", eventId);
            return;
        }

        if (role.equals(UserRole.USER)) {
            UserEntity userEntity = findByUserLogin(login);

            if (eventEntity.getOwner().getId().equals(userEntity.getId())) {
                eventEntity.setStatus(EventStatus.CANCELLED);
                log.info("Event cancelled by owner with id: {}", eventId);
            } else {
                throw new SecurityException("You can only cancel your own events");
            }
        }
    }

}
