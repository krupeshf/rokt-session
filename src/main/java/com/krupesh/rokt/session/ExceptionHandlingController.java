package com.krupesh.rokt.session;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.format.DateTimeParseException;

@ControllerAdvice
public class ExceptionHandlingController extends ResponseEntityExceptionHandler {

  @ExceptionHandler(DateTimeParseException.class)
  protected ResponseEntity<Object> handleDateTimeParseException(
      RuntimeException ex, WebRequest request) {
    return handleExceptionInternal(ex, ex.getMessage(),
        new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler(IOException.class)
  protected ResponseEntity<Object> handleIOException(
      IOException ex, WebRequest request) {
    return handleExceptionInternal(ex, ex.getMessage(),
        new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }
}
