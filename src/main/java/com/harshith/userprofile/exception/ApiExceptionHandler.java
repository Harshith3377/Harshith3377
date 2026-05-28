package com.harshith.userprofile.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(DuplicateUserProfileException.class)
    public ProblemDetail handleDuplicateUserProfile(DuplicateUserProfileException exception) {
        return problemDetail(HttpStatus.CONFLICT, "Duplicate user profile", exception.getMessage());
    }

    @ExceptionHandler(UserProfileNotFoundException.class)
    public ProblemDetail handleUserProfileNotFound(UserProfileNotFoundException exception) {
        return problemDetail(HttpStatus.NOT_FOUND, "User profile not found", exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        String detail = exception.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .orElse("Request validation failed");
        return problemDetail(HttpStatus.BAD_REQUEST, "Invalid request", detail);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException exception) {
        return problemDetail(HttpStatus.BAD_REQUEST, "Invalid request", exception.getMessage());
    }

    @ExceptionHandler(BusinessLogicNotImplementedException.class)
    public ProblemDetail handleBusinessLogicNotImplemented(BusinessLogicNotImplementedException exception) {
        return problemDetail(HttpStatus.NOT_IMPLEMENTED, "Business logic not implemented", exception.getMessage());
    }

    private ProblemDetail problemDetail(HttpStatus status, String title, String detail) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setTitle(title);
        problemDetail.setDetail(detail);
        return problemDetail;
    }
}
