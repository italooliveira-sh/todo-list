package com.italooliveira.projeto.todo_list.exceptions;

import org.springframework.http.HttpStatus;

public class CategoryAlreadyExistsException extends BusinessException {
    public CategoryAlreadyExistsException() {
        super("Já existe uma categoria com este nome", HttpStatus.CONFLICT);
    }
}
