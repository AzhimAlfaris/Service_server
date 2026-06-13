package com.trs.application_service.dto;

public record SensorQueryRequest(String microcontrollerId, String requestType, Integer limit) {
}
