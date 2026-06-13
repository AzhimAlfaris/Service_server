package com.trs.application_service.dto;

import java.time.LocalDateTime;

public record UserResponse(Long id, String email, LocalDateTime createdAt) {
}
