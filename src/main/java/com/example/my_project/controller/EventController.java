package com.example.my_project.controller;

import java.lang.ref.Reference;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.my_project.dto.event.EventCreateAndUpdateResponseDto;
import com.example.my_project.dto.event.EventCreateRequstDto;
import com.example.my_project.dto.event.EventFilterDto;
import com.example.my_project.dto.event.EventResponseDto;
import com.example.my_project.dto.event.EventUpdateRequestDto;
import com.example.my_project.mapper.EventFilterMapper;
import com.example.my_project.mapper.EventMapper;
import com.example.my_project.model.Event;
import com.example.my_project.model.EventFilter;
import com.example.my_project.model.Registration;
import com.example.my_project.service.event.RegistrationService;
import com.example.my_project.service.event.EventService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/events")
@SecurityRequirement(name = "JWT")
public class EventController {

    private final EventService eventService;
    private final EventMapper eventMapper;
    private final RegistrationService eventRegistrationService;
    private final EventFilterMapper eventFilterMapper;
    private static final Logger log = LoggerFactory.getLogger(EventController.class);

    public EventController(
            EventService eventService,
            EventMapper eventMapper,
            RegistrationService eventRegistrationService,
            EventFilterMapper eventFilterMapper) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
        this.eventRegistrationService = eventRegistrationService;
        this.eventFilterMapper = eventFilterMapper;
    }

    @PostMapping
    public ResponseEntity<EventCreateAndUpdateResponseDto> createEvent(
            @RequestBody @Valid EventCreateRequstDto eventCreateRequestDto) {
        log.info("Event create request: {}", eventCreateRequestDto);
        Event event = eventService.createEvent(
                eventMapper.toModel(eventCreateRequestDto));
        EventCreateAndUpdateResponseDto response = eventMapper.toDtoCreateAndUpdate(event);
        log.info("Event create successfully: {}", response);
        return ResponseEntity.status(204).body(response);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponseDto> getEventById(
            @PathVariable Long eventId) {
        log.info("Request to get event by id: {}", eventId);
        Event event = eventService.getEventById(eventId);
        log.info("Event get by id successfully: {}", eventId);
        return ResponseEntity.ok(eventMapper.toDto(event));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> cancelEventById(
            @PathVariable Long eventId) {
        log.info("Request to delete event by id: {}", eventId);
        eventService.cancelEventById(eventId);
        log.info("Event delete by id successfully: {}", eventId);
        return ResponseEntity.status(204).build();
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventCreateAndUpdateResponseDto> updateEvent(
            @PathVariable Long eventId,
            @RequestBody @Valid EventUpdateRequestDto eventUpdateRequestDto) {
        log.info("Received update event request for id {}: {}", eventId, eventUpdateRequestDto);
        Event event = eventService.updateEvent(eventId,
                eventMapper.toModel(eventUpdateRequestDto));
        EventCreateAndUpdateResponseDto response = eventMapper.toDtoCreateAndUpdate(event);
        log.info("Event update successfully: {}", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventResponseDto>> getMyEvents() {
        List<Event> events = eventService.getMyEvents();
        List<EventResponseDto> eventsDtos = events.stream()
                .map(eventMapper::toDto)
                .toList();
        log.info("All events created by the user have been successfully received");
        return ResponseEntity.ok(eventsDtos);
    }

    @PostMapping("/registrations/{eventId}")
    public ResponseEntity<Void> registerForEventById(
            @PathVariable Long eventId) {
        log.info("Request to register for event by id: {}", eventId);
        eventRegistrationService.registerForEventById(eventId);
        log.info("Registration for event by id successfully: {}", eventId);
        return ResponseEntity.status(200).build();

    }

    @DeleteMapping("registrations/cancel/{eventId}")
    public ResponseEntity<Void> cancelRegistrationForEvent(
            @PathVariable Long eventId) {
        log.info("Request to cancel registration for event by id: {}", eventId);
        eventRegistrationService.cancelRegistrationForEvent(eventId);
        log.info("Registration cancellation for event by id successfully: {}", eventId);
        return ResponseEntity.status(204).build();
    }

    @GetMapping("/registrations/my")
    public ResponseEntity<List<EventResponseDto>> getMyRegistration() {
        log.info("Request to get user registrations");
        List<Event> registration = eventRegistrationService.getMyRegistration();
        List<EventResponseDto> eventsDtos = registration.stream()
                .map(eventMapper::toDto)
                .toList();
        log.info("User registrations received successfully, count: {}", eventsDtos.size());
        return ResponseEntity.ok(eventsDtos);
    }

    @GetMapping("/search")
    public ResponseEntity<List<EventResponseDto>> searchEvents(@ModelAttribute @Valid EventFilterDto filter) {
        log.info("Request to search all events by filter");
        List<Event> events = eventService.searchEvents(eventFilterMapper.toModel(filter));
        List<EventResponseDto> eventsDtos = events.stream()
                .map(eventMapper::toDto)
                .toList();
        log.info("USearch all events by filter received successfully, count: {}", eventsDtos.size());
        return ResponseEntity.ok(eventsDtos);
    }
}
