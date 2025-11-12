package com.example.my_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.my_project.entity.EventEntity;

public interface EventRepository extends JpaRepository<EventEntity, Long> {

}
