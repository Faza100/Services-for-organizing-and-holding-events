package com.example.my_project.service.event;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.my_project.entity.EventEntity;
import com.example.my_project.entity.RegistrationEntity;
import com.example.my_project.entity.UserEntity;
import com.example.my_project.enums.EventStatus;
import com.example.my_project.mapper.EventMapper;
import com.example.my_project.model.Event;
import com.example.my_project.repository.EventRepository;
import com.example.my_project.repository.RegistrationRepository;
import com.example.my_project.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final EventMapper eventMapper;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final SecurityContextService securityContextService;
    private static final Logger log = LoggerFactory.getLogger(RegistrationService.class);

    public RegistrationService(
            RegistrationRepository registrationRepository,
            EventMapper eventMapper,
            EventRepository eventRepository,
            UserRepository userRepository,
            SecurityContextService securityContextService) {
        this.registrationRepository = registrationRepository;
        this.eventMapper = eventMapper;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.securityContextService = securityContextService;
    }

    @Transactional
    public void registerForEventById(Long eventId) {
        log.info("Starting registration process for event id: {}", eventId);

        EventEntity event = findByEventId(eventId);

        if (!isActiveEvent(event)) {
            throw new IllegalStateException("Event is not available for registration");
        }

        UserEntity user = findByUserLogin();

        if (user.equals(event.getOwner())) {
            throw new IllegalStateException("The event creator does not need to register");
        }

        if (registrationCheck(user, event)) {
            throw new IllegalStateException("User is already registered for this event");
        }

        if (event.getOccupiedPlaces() >= event.getMaxPlaces()) {
            throw new IllegalStateException("No available places for this event");
        }

        RegistrationEntity newRegistrationEntity = new RegistrationEntity(user, event);
        registrationRepository.save(newRegistrationEntity);

        event.getRegistrations().add(newRegistrationEntity);
        user.getRegistrations().add(newRegistrationEntity);

        event.setOccupiedPlaces(event.getOccupiedPlaces() + 1);

        log.info("User successfully registered for event {}", eventId);
    }

    @Transactional
    public void cancelRegistrationForEvent(Long eventId) {
        log.info("Starting cancellation process for event id: {}", eventId);

        EventEntity event = findByEventId(eventId);

        UserEntity user = findByUserLogin();

        if (!registrationCheck(user, event)) {
            throw new IllegalStateException("User is not registered for this event");
        }

        if (isCancellationTooLate(event)) {
            throw new IllegalStateException("Cancellation is not allowed at this time");
        }

        RegistrationEntity registration = registrationRepository.findRegistrationByUserAndEvent(user, event);

        if (registration == null) {
            throw new EntityNotFoundException("Registration not found");
        }

        event.getRegistrations().remove(registration);
        user.getRegistrations().remove(registration);

        event.setOccupiedPlaces(event.getOccupiedPlaces() - 1);

        registrationRepository.delete(registration);

        log.info("User successfully canceled registration for event {}", eventId);
    }

    @Transactional(readOnly = true)
    public List<Event> getMyRegistration() {
        log.info("Receiving events involving the user");

        UserEntity user = findByUserLogin();

        List<Event> events = user.getRegistrations().stream()
                .map(RegistrationEntity::getEvent)
                .map(eventMapper::toModel)
                .toList();

        log.info("Successful receipt of user-registered events. Found: {}", events.size());
        return events;
    }

    private boolean isActiveEvent(EventEntity event) {
        return !event.getStatus().equals(EventStatus.FINISHED) &&
                !event.getStatus().equals(EventStatus.CANCELLED) &&
                !event.getStatus().equals(EventStatus.ACTIVE);
    }

    private boolean registrationCheck(UserEntity user, EventEntity event) {
        return user.getRegistrations().stream()
                .anyMatch(registration -> registration.getEvent().equals(event));
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
