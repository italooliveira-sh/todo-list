package com.italooliveira.projeto.todo_list.exceptions;

import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends BusinessException {
    public EmailAlreadyExistsException() {
        super("E-mail já cadastrado no sistema", HttpStatus.CONFLICT);
    }
}
