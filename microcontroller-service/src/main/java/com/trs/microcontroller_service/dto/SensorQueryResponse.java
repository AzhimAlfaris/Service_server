package com.trs.microcontroller_service.dto;

import java.util.List;

public record SensorQueryResponse(String status, String message, String microcontrollerId,
                                  List<SensorReadingResponse> readings) {
}
