package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.practicum.request.model.UpdateRequestStatus;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

import static ru.practicum.utils.Templates.EVENT_REQUEST_EMPTY_REQUEST_BODY_EXCEPTION;
import static ru.practicum.utils.Templates.EVENT_REQUEST_REQUEST_IDS_SIZE_VALIDATION_EXCEPTION;
import static ru.practicum.utils.Templates.EVENT_REQUEST_REQUEST_IDS_VALIDATION_EXCEPTION;
import static ru.practicum.utils.Templates.EVENT_REQUEST_STATUS_VALIDATION_EXCEPTION;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@NotNull(message = EVENT_REQUEST_EMPTY_REQUEST_BODY_EXCEPTION)
public class EventRequestStatusUpdateRequestDto {
    @NotNull(message = EVENT_REQUEST_REQUEST_IDS_VALIDATION_EXCEPTION)
    @Size(message = EVENT_REQUEST_REQUEST_IDS_SIZE_VALIDATION_EXCEPTION, min = 1)
    private List<Long> requestIds;

    @NotNull(message = EVENT_REQUEST_STATUS_VALIDATION_EXCEPTION)
    private UpdateRequestStatus status;
}
