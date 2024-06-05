package com.sanlamfintech.bankservice.exception;

import com.sanlamfintech.bankservice.model.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        log.error("Validation errors: {}", errors);
        return ResponseEntity.badRequest().body(new Response(1, "error", errors));
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Response> handleAccountNotFoundException(AccountNotFoundException ex) {
        List<String> errors = List.of(ex.getMessage());
        return ResponseEntity.badRequest().body(new Response(1, "error", errors));
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<Response> handleInsufficientFundsException(InsufficientFundsException ex) {
        List<String> errors = List.of(ex.getMessage());
        return ResponseEntity.badRequest().body(new Response(1, "error", errors));
    }
}
