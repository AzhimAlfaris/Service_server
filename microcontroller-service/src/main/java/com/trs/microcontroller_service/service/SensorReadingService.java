package com.trs.microcontroller_service.service;

import com.trs.microcontroller_service.dto.SensorReadingRequest;
import com.trs.microcontroller_service.dto.SensorReadingResponse;
import com.trs.microcontroller_service.exception.ResourceNotFoundException;
import com.trs.microcontroller_service.model.SensorReading;
import com.trs.microcontroller_service.repository.SensorReadingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SensorReadingService {

    private final SensorReadingRepository sensorReadingRepository;

    public SensorReadingService(SensorReadingRepository sensorReadingRepository) {
        this.sensorReadingRepository = sensorReadingRepository;
    }

    public SensorReadingResponse save(SensorReadingRequest request) {
        SensorReading sensorReading = new SensorReading();
        sensorReading.setMicrocontrollerId(request.microcontrollerId());
        sensorReading.setSensorValue(request.sensorValue());
        sensorReading.setMoisturePercent(request.moisturePercent());
        sensorReading.setSoilCondition(request.soilCondition());
        sensorReading.setAction(request.action());
        sensorReading.setPumpDuration(request.pumpDuration());
        sensorReading.setTimestampSensor(request.timestampSensor());

        return toResponse(sensorReadingRepository.save(sensorReading));
    }

    public SensorReadingResponse getLatestByMicrocontrollerId(String microcontrollerId) {
        return sensorReadingRepository.findTopByMicrocontrollerIdOrderByCreatedAtDesc(microcontrollerId)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Data sensor untuk microcontroller_id " + microcontrollerId + " tidak ditemukan"));
    }

    public List<SensorReadingResponse> getHistoryByMicrocontrollerId(String microcontrollerId, int limit) {
        List<SensorReading> sensorReadings = sensorReadingRepository.findByMicrocontrollerIdOrderByCreatedAtDesc(microcontrollerId);
        if (limit > 0 && limit < sensorReadings.size()) {
            sensorReadings = sensorReadings.subList(0, limit);
        }

        if (sensorReadings.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Riwayat sensor untuk microcontroller_id " + microcontrollerId + " tidak ditemukan");
        }

        return sensorReadings.stream().map(this::toResponse).collect(Collectors.toList());
    }

    private SensorReadingResponse toResponse(SensorReading sensorReading) {
        return new SensorReadingResponse(
                sensorReading.getId(),
                sensorReading.getMicrocontrollerId(),
                sensorReading.getSensorValue(),
                sensorReading.getMoisturePercent(),
                sensorReading.getSoilCondition(),
                sensorReading.getAction(),
                sensorReading.getPumpDuration(),
                sensorReading.getTimestampSensor(),
                sensorReading.getCreatedAt());
    }
}
