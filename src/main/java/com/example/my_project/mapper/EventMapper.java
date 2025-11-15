package com.example.my_project.mapper;

import org.springframework.stereotype.Component;

import com.example.my_project.dto.event.EventCreateAndUpdateResponseDto;
import com.example.my_project.dto.event.EventCreateRequstDto;
import com.example.my_project.dto.event.EventResponseDto;
import com.example.my_project.dto.event.EventUpdateRequestDto;
import com.example.my_project.entity.EventEntity;
import com.example.my_project.entity.LocationEntity;
import com.example.my_project.entity.UserEntity;
import com.example.my_project.model.Event;

@Component
public class EventMapper {

    public EventResponseDto toDto(Event event) {
        return new EventResponseDto(
                event.getOccupiedPlaces(),
                event.getDate(),
                event.getDuration(),
                event.getCost(),
                event.getMaxPlaces(),
                event.getLocationId(),
                event.getName(),
                event.getId(),
                event.getOwnerId(),
                event.getStatus());
    }

    public EventCreateAndUpdateResponseDto toDtoCreateAndUpdate(Event event) {
        return new EventCreateAndUpdateResponseDto(
                event.getDate(),
                event.getDuration(),
                event.getCost(),
                event.getMaxPlaces(),
                event.getLocationId(),
                event.getName());
    }

    public Event toModel(EventCreateRequstDto eventCreateDto) {
        return new Event(
                eventCreateDto.getDate(),
                eventCreateDto.getDuration(),
                eventCreateDto.getCost(),
                eventCreateDto.getMaxPlaces(),
                eventCreateDto.getLocationId(),
                eventCreateDto.getName());
    }

    public Event toModel(EventUpdateRequestDto eventUpdateDto) {
        return new Event(
                eventUpdateDto.getDate(),
                eventUpdateDto.getDuration(),
                eventUpdateDto.getCost(),
                eventUpdateDto.getMaxPlaces(),
                eventUpdateDto.getLocationId(),
                eventUpdateDto.getName());
    }

    public Event toModel(EventEntity eventEntity) {
        return new Event(
                eventEntity.getOccupiedPlaces(),
                eventEntity.getDate(),
                eventEntity.getDuration(),
                eventEntity.getCost(),
                eventEntity.getMaxPlaces(),
                eventEntity.getLocation().getId(),
                eventEntity.getName(),
                eventEntity.getId(),
                eventEntity.getOwner().getId(),
                eventEntity.getStatus());
    }

    public EventEntity toEntity(Event event, LocationEntity location, UserEntity owner) {
        return new EventEntity(
                event.getOccupiedPlaces(),
                event.getDate(),
                event.getDuration(),
                event.getCost(),
                event.getMaxPlaces(),
                location,
                event.getName(),
                event.getId(),
                owner,
                event.getStatus());
    }

}
