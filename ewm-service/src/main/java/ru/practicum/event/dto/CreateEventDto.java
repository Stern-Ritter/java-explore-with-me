package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.practicum.location.dto.LocationDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.utils.Templates.EVENT_ANNOTATION_LENGTH_VALIDATION_EXCEPTION;
import static ru.practicum.utils.Templates.EVENT_DESCRIPTION_LENGTH_VALIDATION_EXCEPTION;
import static ru.practicum.utils.Templates.EVENT_EMPTY_ANNOTATION_VALIDATION_EXCEPTION;
import static ru.practicum.utils.Templates.EVENT_EMPTY_DESCRIPTION_VALIDATION_EXCEPTION;
import static ru.practicum.utils.Templates.EVENT_EMPTY_REQUEST_BODY_EXCEPTION;
import static ru.practicum.utils.Templates.EVENT_EMPTY_TITLE_VALIDATION_EXCEPTION;
import static ru.practicum.utils.Templates.EVENT_NULL_CATEGORY_VALIDATION_EXCEPTION;
import static ru.practicum.utils.Templates.EVENT_NULL_EVENT_DATE_VALIDATION_EXCEPTION;
import static ru.practicum.utils.Templates.EVENT_NULL_LOCATION_VALIDATION_EXCEPTION;
import static ru.practicum.utils.Templates.EVENT_TITLE_LENGTH_VALIDATION_EXCEPTION;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@NotNull(message = EVENT_EMPTY_REQUEST_BODY_EXCEPTION)
public class CreateEventDto {
    @NotEmpty(message = EVENT_EMPTY_ANNOTATION_VALIDATION_EXCEPTION)
    @Size(message = EVENT_ANNOTATION_LENGTH_VALIDATION_EXCEPTION, min = 20, max = 2000)
    private String annotation;

    @NotNull(message = EVENT_NULL_CATEGORY_VALIDATION_EXCEPTION)
    private Long category;

    @NotEmpty(message = EVENT_EMPTY_DESCRIPTION_VALIDATION_EXCEPTION)
    @Size(message = EVENT_DESCRIPTION_LENGTH_VALIDATION_EXCEPTION, min = 20, max = 7000)
    private String description;

    @NotNull(message = EVENT_NULL_EVENT_DATE_VALIDATION_EXCEPTION)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull(message = EVENT_NULL_LOCATION_VALIDATION_EXCEPTION)
    private LocationDto location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    @NotEmpty(message = EVENT_EMPTY_TITLE_VALIDATION_EXCEPTION)
    @Size(message = EVENT_TITLE_LENGTH_VALIDATION_EXCEPTION, min = 3, max = 120)
    private String title;
}
