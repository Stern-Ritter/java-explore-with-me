package ru.practicum.utils;

public class Templates {
    public static final String ENDPOINT_HIT_EMPTY_REQUEST_BODY_EXCEPTION = "Request body should contain endpoint hit information";
    public static final String ENDPOINT_HIT_APPLICATION_NAME_VALIDATION_EXCEPTION = "Endpoint hit application date should not be null";
    public static final String ENDPOINT_HIT_URI_VALIDATION_EXCEPTION = "Endpoint hit uri should not be null";
    public static final String ENDPOINT_HIT_IP_VALIDATION_EXCEPTION = "Endpoint hit ip should not be null";
    public static final String ENDPOINT_HIT_REQUESTED_DATE_VALIDATION_EXCEPTION = "Endpoint hit requested date should not be null";


    public static final String GET_STATS_TEMPLATE = "GET '/stats?start=%s&end=%s&uris=%s&unique=%s'";
    public static final String POST_ENDPOINT_HIT = "POST '/hit', body={%s}";
}
