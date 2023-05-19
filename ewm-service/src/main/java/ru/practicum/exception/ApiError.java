package ru.practicum.exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiError {
    private final String status;
    private final String reason;
    private final String message;
    private final LocalDateTime timestamp;
}
