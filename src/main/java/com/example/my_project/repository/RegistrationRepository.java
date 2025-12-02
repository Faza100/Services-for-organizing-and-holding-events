package com.example.my_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.my_project.entity.EventEntity;
import com.example.my_project.entity.RegistrationEntity;
import com.example.my_project.entity.UserEntity;

@Repository
public interface RegistrationRepository extends JpaRepository<RegistrationEntity, Long> {

    RegistrationEntity findRegistrationByUserAndEvent(UserEntity user, EventEntity event);
}
