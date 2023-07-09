package ru.practicum.utils;

import ru.practicum.event.model.Event;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

    public static long calculateRating(List<Event> events) {
        return Math.round(events.stream()
                .mapToInt(event -> event.getLikes().size() - event.getDislikes().size())
                .average()
                .orElseGet(() -> 0));
    }

    public static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    public static LocalDateTime parseDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(date, formatter);
    }
}
