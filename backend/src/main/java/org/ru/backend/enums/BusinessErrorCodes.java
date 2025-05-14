package org.ru.backend.enums;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BusinessErrorCodes {
    NO_CODE(0, HttpStatus.NOT_IMPLEMENTED, "No code"),
    INCORRECT_CURRENT_PASSWORD(300, HttpStatus.BAD_REQUEST, "Incorrect password"),
    ACCOUNT_LOCKED(302, HttpStatus.FORBIDDEN, "User account is locked"),
    ACCOUNT_DISABLED(303, HttpStatus.FORBIDDEN, "User account is disabled"),
    BAD_CREDENTIALS(304, HttpStatus.UNAUTHORIZED, "Login and/or password is incorrect"),
    NEW_PASSWORD_DOES_NOT_MATCH(301, HttpStatus.BAD_REQUEST, "The new password does not match"),
    USER_ALREADY_EXISTS(305, HttpStatus.BAD_REQUEST, "User already exists"),
    INVALID_TOKEN(306, HttpStatus.UNAUTHORIZED, "Invalid token"),
    TOKEN_EXPIRED(307, HttpStatus.UNAUTHORIZED, "Token has expired"),
    USER_NOT_FOUND(308, HttpStatus.NOT_FOUND, "User not found"),
    COURSE_NOT_FOUND(1001, HttpStatus.NOT_FOUND, "Course not found"),
    LEVEL_NOT_FOUND(1002, HttpStatus.NOT_FOUND,"Level not found"),
    WORD_NOT_FOUND(1003, HttpStatus.NOT_FOUND,"Word not found"),
    USER_EXISTS(2001, HttpStatus.CONFLICT, "User already exists"),
    FILE_UPLOAD_FAILED(3001, HttpStatus.PAYLOAD_TOO_LARGE, "File upload failed"),
    COURSE_ACCESS_DENIED(4001, HttpStatus.FORBIDDEN, "Course access denied"),
    VALIDATION_ERROR(4002, HttpStatus.BAD_REQUEST, "Validation error"),
    NO_ACTIVE_WORD(4003, HttpStatus.BAD_REQUEST, "No active word"),
    INVALID_ROLE(4004, HttpStatus.BAD_REQUEST, "Invalid role name"),
    INVALID_OPERATION(4005, HttpStatus.BAD_REQUEST, "Invalid operation"),
    INVALID_ACTIVATION_TOKEN(4006, HttpStatus.BAD_REQUEST,  "Invalid activation token"),
    EXPIRED_ACTIVATION_TOKEN(4007, HttpStatus.BAD_REQUEST, "Activation token has expired"),
    TOKEN_ALREADY_USED(4008, HttpStatus.BAD_REQUEST, "Token has already been used"),
    INTERNAL_SERVER_ERROR(5000, HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    SEARCH_FAILED(4009, HttpStatus.BAD_REQUEST , "Search failed" ),
    NOT_ENOUGH_WORDS(4010, HttpStatus.BAD_REQUEST , "Not enough words"),;



    private final int code;
    private final HttpStatus httpStatus;
    private final String description;

    BusinessErrorCodes(int code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.description = description;
    }
}