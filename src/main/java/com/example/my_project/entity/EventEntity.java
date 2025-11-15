package com.example.my_project.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.my_project.enums.EventStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "events")
public class EventEntity {

    @Column(nullable = false)
    private Integer occupiedPlaces;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private Integer duration;

    @Column(nullable = false)
    private Integer cost;

    @Column(nullable = false)
    private Integer maxPlaces;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private LocationEntity location;

    @Column(nullable = false)
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;

    @Column(nullable = false)
    private EventStatus status;

    @ManyToMany(mappedBy = "registeredEvents", fetch = FetchType.LAZY)
    private List<UserEntity> registeredUsers = new ArrayList<>();

    public EventEntity(Integer occupiedPlaces,
            LocalDateTime date,
            Integer duration,
            Integer cost,
            Integer maxPlaces,
            LocationEntity location,
            String name,
            Long id,
            UserEntity owner,
            EventStatus status) {
        this.occupiedPlaces = occupiedPlaces;
        this.date = date;
        this.duration = duration;
        this.cost = cost;
        this.maxPlaces = maxPlaces;
        this.location = location;
        this.name = name;
        this.id = id;
        this.owner = owner;
        this.status = status;
    }

    public EventEntity() {

    }

    public Integer getOccupiedPlaces() {
        return occupiedPlaces;
    }

    public void setOccupiedPlaces(Integer occupiedPlaces) {
        this.occupiedPlaces = occupiedPlaces;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getMaxPlaces() {
        return maxPlaces;
    }

    public void setMaxPlaces(Integer maxPlaces) {
        this.maxPlaces = maxPlaces;
    }

    public LocationEntity getLocation() {
        return location;
    }

    public void setLocation(LocationEntity location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public List<UserEntity> getRegisteredUsers() {
        return registeredUsers;
    }

    public void setRegisteredUsers(List<UserEntity> registeredUsers) {
        this.registeredUsers = registeredUsers;
    }

    public void updateStatus() {

        if (this.status == EventStatus.CANCELLED) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime eventEnd = this.date.plusMinutes(this.duration);

        if (now.isBefore(this.date)) {
            this.status = EventStatus.PLANNED;
        } else if (now.isAfter(eventEnd)) {
            this.status = EventStatus.FINISHED;
        } else {
            this.status = EventStatus.ACTIVE;
        }
    }
}
