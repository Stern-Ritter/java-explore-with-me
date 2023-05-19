package ru.practicum.utils;

import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class Utils {
    public static final String APPLICATION_NAME = "ewm-main-service";
    public static <T> T coalesce(T first, T second) {
        return first == null ? second : first;
    }

    public static int calculateFirstPageNumber(int offset, int limit) {
        return offset / limit;
    }

    public static long countConfirmedRequests(List<Request> requests) {
        return requests.stream()
                .map(request -> request.getStatus() == RequestStatus.CONFIRMED).count();
    }

    public static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    public static LocalDateTime parseDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(date, formatter);
    }

    public static Throwable findExceptionRootCause(Throwable throwable) {
        Objects.requireNonNull(throwable);
        Throwable rootCause = throwable;
        while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
            rootCause = rootCause.getCause();
        }
        return rootCause;
    }
}
