package com.fintech.database.handler;

import com.fintech.database.dto.response.ErrorRs;
import com.fintech.database.dto.response.ErrorRsWithParam;
import com.fintech.database.exception.ObjectAlreadyExistsException;
import com.fintech.database.exception.ObjectNotFoundException;
import com.fintech.database.exception.RelatedEntityNotFound;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorRsWithParam> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> params = new HashMap<>();
        ex.getConstraintViolations().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = ((FieldError) error).getDefaultMessage();
            params.put(fieldName, errorMessage);
        });
        String error = "Not valid fields";
        log.error(error);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorRsWithParam.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .error(error)
                        .params(params).build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRsWithParam> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> params = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            params.put(fieldName, errorMessage);
        });
        String error = "Not valid fields";
        log.error(error);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorRsWithParam.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .error(error)
                        .params(params).build());
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<ErrorRs> handleServiceUnavailableException(WebClientResponseException ex) {
        log.error(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .header("Retry-After", "3600")
                .body(ErrorRs.builder()
                        .status(HttpStatus.SERVICE_UNAVAILABLE)
                        .error(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(ObjectAlreadyExistsException.class)
    public ResponseEntity<ErrorRs> handleObjectAlreadyExistsException(ObjectAlreadyExistsException ex) {
        log.error(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorRs.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .error(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<ErrorRs> handleObjectNotFoundException(ObjectNotFoundException ex) {
        log.error(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorRs.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .error(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(RelatedEntityNotFound.class)
    public ResponseEntity<ErrorRs> handleRelatedEntityNotFound(RelatedEntityNotFound ex) {
        log.error(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorRs.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .error(ex.getMessage())
                        .build());
    }
}