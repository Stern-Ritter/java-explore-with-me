package ru.practicum.utils;

public class Templates {
    //Validation
    public static final String USER_EMPTY_REQUEST_BODY_EXCEPTION = "Request body does not contain user data";
    public static final String USER_EMPTY_NAME_VALIDATION_EXCEPTION = "User name should not be empty";
    public static final String USER_NAME_LENGTH_VALIDATION_EXCEPTION = "User name length should be between 2 and 250 characters";
    public static final String USER_EMPTY_EMAIL_VALIDATION_EXCEPTION = "User email should not be empty";
    public static final String USER_EMAIL_LENGTH_VALIDATION_EXCEPTION = "User email length should be between 6 and 254 characters";
    public static final String USER_EMAIL_VALIDATION_EXCEPTION = "User email should contain '@' character";

    public static final String CATEGORY_EMPTY_REQUEST_BODY_EXCEPTION = "Request body does not contain category data";
    public static final String CATEGORY_NAME_VALIDATION_EXCEPTION = "Category name should not be empty";
    public static final String CATEGORY_NAME_LENGTH_VALIDATION_EXCEPTION = "Category name length should be between 2 and 50 characters";

    public static final String EVENT_EMPTY_REQUEST_BODY_EXCEPTION = "Request body does not contain event data";
    public static final String EVENT_EMPTY_ANNOTATION_VALIDATION_EXCEPTION = "Event annotation should not be empty";
    public static final String EVENT_ANNOTATION_LENGTH_VALIDATION_EXCEPTION = "Event annotation length should be between 20 and 2000 characters";
    public static final String EVENT_NULL_CATEGORY_VALIDATION_EXCEPTION = "Event category should not be null";
    public static final String EVENT_EMPTY_DESCRIPTION_VALIDATION_EXCEPTION = "Event description should not be empty";
    public static final String EVENT_DESCRIPTION_LENGTH_VALIDATION_EXCEPTION = "Event description length should be between 20 and 7000 characters";
    public static final String EVENT_NULL_EVENT_DATE_VALIDATION_EXCEPTION = "Event event date should not be null";
    public static final String EVENT_NULL_LOCATION_VALIDATION_EXCEPTION = "Event location should not be null";
    public static final String EVENT_EMPTY_TITLE_VALIDATION_EXCEPTION = "Event title should not be empty";
    public static final String EVENT_TITLE_LENGTH_VALIDATION_EXCEPTION = "Event title length should be between 3 and 120 characters";

    public static final String EVENT_REQUEST_EMPTY_REQUEST_BODY_EXCEPTION = "Request body does not contain event request data";
    public static final String EVENT_REQUEST_REQUEST_IDS_VALIDATION_EXCEPTION = "Event request request ids should not be null";
    public static final String EVENT_REQUEST_REQUEST_IDS_SIZE_VALIDATION_EXCEPTION = "Event request request ids size should not be less than 1";
    public static final String EVENT_REQUEST_STATUS_VALIDATION_EXCEPTION = "Event request status should not be null";

    public static final String COMPILATION_EMPTY_REQUEST_BODY_EXCEPTION = "Request body does not contain compilation date";
    public static final String COMPILATION_EMPTY_TITLE_VALIDATION_EXCEPTION = "Compilation title should not be empty";
    public static final String COMPILATION_TITLE_LENGTH_VALIDATION_EXCEPTION = "Compilation title length should be between 2 and 50 characters";

    //Exceptions
    public static final String USER_NOT_EXISTS_TEMPLATE = "User with id=%s was not found";
    public static final String CATEGORY_NOT_EXISTS_TEMPLATE = "Category with id=%s was not found";
    public static final String EVENT_NOT_EXISTS_TEMPLATE = "Event with id=%s was not found";
    public static final String EVENT_EVENT_DATE_VALIDATION_EXCEPTION = "Cannot publish the event because event date minimum offset of current time: %s";
    public static final String EVENT_FILTER_DATES_VALIDATION_TEMPLATE = "The end date should not be earlier than the start date";
    public static final String EVENT_PUBLISH_STATE_VALIDATION_EXCEPTION = "Cannot publish the event because it's not in the right state: %s";
    public static final String EVENT_CANCEL_STATE_VALIDATION_EXCEPTION = "Cannot cancel the event because it's not in the right state: %s";
    public static final String EVENT_REQUEST_PARTICIPANT_LIMIT_EXCEPTION = "The participant limit has been reached";
    public static final String EVENT_REQUEST_CONFIRMATION_INCORRECT_STATUS_EXCEPTION = "Cannot confirm the event request because it's not in the right state";

    public static final String REQUEST_NOT_EXISTS_TEMPLATE = "Request with id=%s was not found";
    public static final String REQUEST_INCORRECT_REQUESTER_EXCEPTION = "The initiator of the event cannot add a request to participate in his event";
    public static final String REQUEST_INCORRECT_EVENT_STATUS_EXCEPTION = "Cannot participate in an unpublished event";
    public static final String REQUEST_EVENT_LIMIT_OF_PARTICIPATION_EXCEPTION = "The event has reached the limit of participation requests";

    public static final String COMPILATION_NOT_EXISTS_TEMPLATE = "Compilation with id=%s was not found";
}
