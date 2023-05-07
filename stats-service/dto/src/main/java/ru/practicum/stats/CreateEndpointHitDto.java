package ru.practicum.stats;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;

import static ru.practicum.utils.Templates.ENDPOINT_HIT_APPLICATION_NAME_VALIDATION_EXCEPTION;
import static ru.practicum.utils.Templates.ENDPOINT_HIT_EMPTY_REQUEST_BODY_EXCEPTION;
import static ru.practicum.utils.Templates.ENDPOINT_HIT_IP_VALIDATION_EXCEPTION;
import static ru.practicum.utils.Templates.ENDPOINT_HIT_REQUESTED_DATE_VALIDATION_EXCEPTION;
import static ru.practicum.utils.Templates.ENDPOINT_HIT_URI_VALIDATION_EXCEPTION;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@NotNull(message = ENDPOINT_HIT_EMPTY_REQUEST_BODY_EXCEPTION)
public class CreateEndpointHitDto {
    @NotNull(message = ENDPOINT_HIT_APPLICATION_NAME_VALIDATION_EXCEPTION)
    private String app;

    @NotNull(message = ENDPOINT_HIT_URI_VALIDATION_EXCEPTION)
    private String uri;

    @NotNull(message = ENDPOINT_HIT_IP_VALIDATION_EXCEPTION)
    private String ip;

    @NotNull(message = ENDPOINT_HIT_REQUESTED_DATE_VALIDATION_EXCEPTION)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
