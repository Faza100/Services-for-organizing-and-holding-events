package com.example.my_project.model;

import java.time.LocalDateTime;

public class Registration {

    private Long id;

    private Long userId;

    private Long eventId;

    private LocalDateTime registrationDate;

    public Registration(
            Long userId,
            Long eventId) {
        this.userId = userId;
        this.eventId = eventId;
        this.registrationDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

}
