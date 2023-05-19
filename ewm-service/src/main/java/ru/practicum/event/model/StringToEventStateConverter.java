package ru.practicum.event.model;

import org.springframework.core.convert.converter.Converter;

public class StringToEventStateConverter implements Converter<String, EventState> {
    private static final String DATA_CONVERSION_ERROR = "Unknown state: %s";

    @Override
    public EventState convert(String source) {
        try {
            return EventState.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format(DATA_CONVERSION_ERROR, source));
        }
    }
}
