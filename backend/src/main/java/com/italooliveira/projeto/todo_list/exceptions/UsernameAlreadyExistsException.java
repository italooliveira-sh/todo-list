package com.italooliveira.projeto.todo_list.exceptions;

import org.springframework.http.HttpStatus;

public class UsernameAlreadyExistsException extends BusinessException {
    public UsernameAlreadyExistsException() {
        super("Nome de usuário já cadastrado", HttpStatus.CONFLICT);
    }
}
