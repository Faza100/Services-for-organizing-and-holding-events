package com.example.my_progect.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.my_progect.model.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

}
