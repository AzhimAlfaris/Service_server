package com.trs.microcontroller_service.controller;

import com.trs.microcontroller_service.dto.SensorReadingRequest;
import com.trs.microcontroller_service.dto.SensorReadingResponse;
import com.trs.microcontroller_service.service.SensorReadingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sensor-readings")
@Slf4j
@RequiredArgsConstructor
public class SensorReadingController {

    private final SensorReadingService sensorReadingService;

    @PostMapping
    public ResponseEntity<SensorReadingResponse> save(@RequestBody SensorReadingRequest request) {
        log.info("Request save sensor reading microcontrollerId={}", request.microcontrollerId());
        return ResponseEntity.status(HttpStatus.CREATED).body(sensorReadingService.save(request));
    }

    @GetMapping("/latest/{microcontrollerId}")
    public ResponseEntity<SensorReadingResponse> getLatest(@PathVariable String microcontrollerId) {
        log.info("Request latest sensor reading for microcontrollerId={}", microcontrollerId);
        return ResponseEntity.ok(sensorReadingService.getLatestByMicrocontrollerId(microcontrollerId));
    }

    @GetMapping("/history/{microcontrollerId}")
    public ResponseEntity<List<SensorReadingResponse>> getHistory(
            @PathVariable String microcontrollerId,
            @RequestParam(defaultValue = "10") int limit) {
        log.info("Request sensor reading history for microcontrollerId={} limit={}", microcontrollerId, limit);
        return ResponseEntity.ok(sensorReadingService.getHistoryByMicrocontrollerId(microcontrollerId, limit));
    }
}
