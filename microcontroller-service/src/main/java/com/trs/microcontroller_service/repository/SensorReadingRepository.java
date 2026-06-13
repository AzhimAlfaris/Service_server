package com.trs.microcontroller_service.repository;

import com.trs.microcontroller_service.model.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SensorReadingRepository extends JpaRepository<SensorReading, Long> {

    Optional<SensorReading> findTopByMicrocontrollerIdOrderByCreatedAtDesc(String microcontrollerId);

    List<SensorReading> findByMicrocontrollerIdOrderByCreatedAtDesc(String microcontrollerId);

    List<SensorReading> findTop10ByMicrocontrollerIdOrderByCreatedAtDesc(String microcontrollerId);
}
