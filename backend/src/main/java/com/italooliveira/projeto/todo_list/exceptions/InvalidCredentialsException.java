package com.italooliveira.projeto.todo_list.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends BusinessException {
    public InvalidCredentialsException() {
        super("Credenciais inválidas", HttpStatus.UNAUTHORIZED);
    }
}
