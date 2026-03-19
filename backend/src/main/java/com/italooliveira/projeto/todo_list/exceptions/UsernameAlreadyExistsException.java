package com.italooliveira.projeto.todo_list.exceptions;

public class UsernameAlreadyExistsException extends BusinessException {
    public UsernameAlreadyExistsException() {
        super("Nome de usuário já cadastrado");
    }
}
