package ru.practicum.compilation.view;

import ru.practicum.event.view.EventShortView;

import java.util.List;

public interface CompilationView {
    Long getId();

    String getTitle();

    Boolean getPinned();

    List<EventShortView> getEvents();
}
