package com.example.my_project.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.my_project.entity.EventEntity;
import com.example.my_project.enums.EventStatus;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {

        @Query("""
                        SELECT e FROM EventEntity e
                        WHERE (:name IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%')))
                        AND (:placesMin IS NULL OR e.maxPlaces >= :placesMin)
                        AND (:placesMax IS NULL OR e.maxPlaces <= :placesMax)
                        AND (:dateStartAfter IS NULL OR e.date >= :dateStartAfter)
                        AND (:dateStartBefore IS NULL OR e.date <= :dateStartBefore)
                        AND (:costMin IS NULL OR e.cost >= :costMin)
                        AND (:costMax IS NULL OR e.cost <= :costMax)
                        AND (:durationMin IS NULL OR e.duration >= :durationMin)
                        AND (:durationMax IS NULL OR e.duration <= :durationMax)
                        AND (:locationId IS NULL OR e.location.id = :locationId)
                        AND (:eventStatus IS NULL OR e.status = :eventStatus)
                        """)
        List<EventEntity> findEvents(
                        @Param("name") String name,
                        @Param("placesMin") Integer placesMin,
                        @Param("placesMax") Integer placesMax,
                        @Param("dateStartAfter") LocalDateTime dateStartAfter,
                        @Param("dateStartBefore") LocalDateTime dateStartBefore,
                        @Param("costMin") Integer costMin,
                        @Param("costMax") Integer costMax,
                        @Param("durationMin") Integer durationMin,
                        @Param("durationMax") Integer durationMax,
                        @Param("locationId") Long locationId,
                        @Param("eventStatus") EventStatus eventStatus);

}
