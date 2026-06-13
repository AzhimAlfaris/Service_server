package com.trs.microcontroller_service.dto;

public record SensorQueryRequest(String microcontrollerId, String requestType, Integer limit) {
}
