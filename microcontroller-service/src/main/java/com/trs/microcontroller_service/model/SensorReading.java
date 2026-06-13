package com.trs.microcontroller_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "sensor_readings")
public class SensorReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "microcontroller_id", nullable = false, length = 100)
    private String microcontrollerId;

    @Column(name = "sensor_value", nullable = false, length = 255)
    private String sensorValue;

    @Column(name = "moisture_percent", nullable = false, length = 255)
    private String moisturePercent;

    @Column(name = "soil_condition", nullable = false, length = 255)
    private String soilCondition;

    @Column(name = "action", nullable = false, length = 255)
    private String action;

    @Column(name = "pump_duration", nullable = false, length = 255)
    private String pumpDuration;

    @Column(name = "timestamp_sensor", nullable = false, length = 255)
    private String timestampSensor;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMicrocontrollerId() {
        return microcontrollerId;
    }

    public void setMicrocontrollerId(String microcontrollerId) {
        this.microcontrollerId = microcontrollerId;
    }

    public String getSensorValue() {
        return sensorValue;
    }

    public void setSensorValue(String sensorValue) {
        this.sensorValue = sensorValue;
    }

    public String getMoisturePercent() {
        return moisturePercent;
    }

    public void setMoisturePercent(String moisturePercent) {
        this.moisturePercent = moisturePercent;
    }

    public String getSoilCondition() {
        return soilCondition;
    }

    public void setSoilCondition(String soilCondition) {
        this.soilCondition = soilCondition;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPumpDuration() {
        return pumpDuration;
    }

    public void setPumpDuration(String pumpDuration) {
        this.pumpDuration = pumpDuration;
    }

    public String getTimestampSensor() {
        return timestampSensor;
    }

    public void setTimestampSensor(String timestampSensor) {
        this.timestampSensor = timestampSensor;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
