package com.example.my_project.service.event;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.my_project.entity.EventEntity;
import com.example.my_project.entity.UserEntity;
import com.example.my_project.mapper.EventMapper;
import com.example.my_project.model.Event;
import com.example.my_project.repository.EventRepository;
import com.example.my_project.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EventRegistrationService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final UserRepository userRepository;
    private final SecurityContextService securityContextService;
    private final EventBusinessRulesService eventBusinessRulesService;
    private static final Logger log = LoggerFactory.getLogger(EventRegistrationService.class);

    public EventRegistrationService(
            EventRepository eventRepository,
            EventMapper eventMapper,
            UserRepository userRepository,
            SecurityContextService securityContextService,
            EventBusinessRulesService eventBusinessRulesService) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.userRepository = userRepository;
        this.securityContextService = securityContextService;
        this.eventBusinessRulesService = eventBusinessRulesService;
    }

    @Transactional
    public void registerForEventById(Long eventId) {
        log.info("Starting registration process for event id: {}", eventId);

        EventEntity event = findByEventId(eventId);

        if (!eventBusinessRulesService.isActiveEvent(event)) {
            throw new IllegalStateException("Event is not available for registration");
        }

        UserEntity user = findByUserLogin();

        if (user.equals(event.getOwner())) {
            throw new IllegalStateException("The event creator does not need to register");
        }

        if (event.getRegisteredUsers().contains(user)) {
            throw new IllegalStateException("User is already registered for this event");
        }

        if (event.getOccupiedPlaces() >= event.getMaxPlaces()) {
            throw new IllegalStateException("No available places for this event");
        }

        event.getRegisteredUsers().add(user);
        event.setOccupiedPlaces(event.getOccupiedPlaces() + 1);
        user.getRegisteredEvents().add(event);

        log.info("User successfully registered for event {}", eventId);
    }

    @Transactional
    public void cancelRegistrationForEvent(Long eventId) {
        log.info("Starting cancellation process for event id: {}", eventId);

        EventEntity event = findByEventId(eventId);

        UserEntity user = findByUserLogin();

        if (!event.getRegisteredUsers().contains(user)) {
            throw new IllegalStateException("User is not registered for this event");
        }

        if (isCancellationTooLate(event)) {
            throw new IllegalStateException("Cancellation is not allowed at this time");
        }

        event.getRegisteredUsers().remove(user);
        event.setOccupiedPlaces(event.getOccupiedPlaces() - 1);

        user.getRegisteredEvents().remove(event);

        log.info("User successfully canceled registration for event {}", eventId);
    }

    @Transactional(readOnly = true)
    public List<Event> getMyRegistration() {
        log.info("Receiving events involving the user");

        UserEntity user = findByUserLogin();

        log.info("successful receipt of user-created events");
        return user.getRegisteredEvents().stream()
                .map(eventMapper::toModel)
                .toList();
    }

    private boolean isCancellationTooLate(EventEntity event) {
        LocalDateTime cancellationDeadline = event.getDate().minusHours(1);
        return LocalDateTime.now().isAfter(cancellationDeadline);
    }

    private EventEntity findByEventId(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + eventId));
    }

    private UserEntity findByUserLogin() {
        String receivedLogin = securityContextService.getCurrentUserLogin();
        return userRepository.getUserByLogin(receivedLogin)
                .orElseThrow(() -> new EntityNotFoundException("User not found with login: " + receivedLogin));
    }

}
