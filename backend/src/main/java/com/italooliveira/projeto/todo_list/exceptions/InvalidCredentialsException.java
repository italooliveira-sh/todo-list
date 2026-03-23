package com.italooliveira.projeto.todo_list.exceptions;

public class InvalidCredentialsException extends BusinessException {
    public InvalidCredentialsException() {
        super("E-mail ou senha incorretos.");
    }
}
