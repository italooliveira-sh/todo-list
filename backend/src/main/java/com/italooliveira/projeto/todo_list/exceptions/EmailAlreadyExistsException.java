package com.italooliveira.projeto.todo_list.exceptions;

public class EmailAlreadyExistsException extends BusinessException {
    public EmailAlreadyExistsException() {
        super("E-mail já cadastrado no sistema");
    }
}
