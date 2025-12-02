package com.example.my_project.mapper;

import org.springframework.stereotype.Component;

import com.example.my_project.dto.event.EventFilterDto;
import com.example.my_project.model.EventFilter;

@Component
public class EventFilterMapper {

    public EventFilterDto toDto(EventFilter eventFilter) {
        return new EventFilterDto(
                eventFilter.getDurationMax(),
                eventFilter.getDateStartBefore(),
                eventFilter.getPlacesMin(),
                eventFilter.getLocationId(),
                eventFilter.getEventStatus(),
                eventFilter.getName(),
                eventFilter.getPlacesMax(),
                eventFilter.getCostMin(),
                eventFilter.getDateStartAfter(),
                eventFilter.getCostMax(),
                eventFilter.getDurationMin());
    }

    public EventFilter toModel(EventFilterDto eventFilterDto) {
        return new EventFilter(
                eventFilterDto.getDurationMax(),
                eventFilterDto.getDateStartBefore(),
                eventFilterDto.getPlacesMin(),
                eventFilterDto.getLocationId(),
                eventFilterDto.getEventStatus(),
                eventFilterDto.getName(),
                eventFilterDto.getPlacesMax(),
                eventFilterDto.getCostMin(),
                eventFilterDto.getDateStartAfter(),
                eventFilterDto.getCostMax(),
                eventFilterDto.getDurationMin());
    }

}
