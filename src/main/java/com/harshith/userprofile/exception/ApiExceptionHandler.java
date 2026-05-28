package com.harshith.userprofile.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(BusinessLogicNotImplementedException.class)
    public ProblemDetail handleBusinessLogicNotImplemented(BusinessLogicNotImplementedException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_IMPLEMENTED);
        problemDetail.setTitle("Business logic not implemented");
        problemDetail.setDetail(exception.getMessage());
        return problemDetail;
    }
}
