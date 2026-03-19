package com.italooliveira.projeto.todo_list.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorMessage {

    private final OffsetDateTime timestamp;
    private final int status;
    private final String statusText;
    private final String message;
    private final String path;
    private final String method;
    private Map<String, String> errors;

    // Construtor para erros comuns (Negócio, Not Found, etc)
    public ApiErrorMessage(HttpServletRequest request, HttpStatus status, String message) {
        this.timestamp = OffsetDateTime.now();
        this.status = status.value();
        this.statusText = status.getReasonPhrase();
        this.message = message;
        this.path = request.getRequestURI();
        this.method = request.getMethod();
    }

    // Construtor para erros de validação (@Valid)
    public ApiErrorMessage(HttpServletRequest request, HttpStatus status, String message, BindingResult result) {
        this(request, status, message);
        this.addErrors(result);
    }

    private void addErrors(BindingResult result) {
        this.errors = new HashMap<>();
        for (FieldError error : result.getFieldErrors()) {
            this.errors.put(error.getField(), error.getDefaultMessage());
        }
    }
}
