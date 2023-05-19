package ru.practicum.location.mapper;

import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.model.Location;
import ru.practicum.location.view.LocationView;

public class LocationMapper {
    public static Location toLocation(LocationDto locationDto) {
        Location location = new Location();
        location.setLat(locationDto.getLat());
        location.setLon(locationDto.getLon());
        return location;
    }

    public static LocationDto toLocationDto(Location location) {
        LocationDto locationDto = new LocationDto();
        locationDto.setLat(location.getLat());
        locationDto.setLon(location.getLon());
        return locationDto;
    }

    public static LocationDto toLocationDto(LocationView locationView) {
        LocationDto locationDto = new LocationDto();
        locationDto.setLat(locationView.getLat());
        locationDto.setLon(locationView.getLon());
        return locationDto;
    }
}
