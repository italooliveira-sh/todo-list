package com.italooliveira.projeto.todo_list.exceptions;

import org.springframework.http.HttpStatus;

public class ForbiddenActionException extends BusinessException {
    public ForbiddenActionException() {
        super("Você não tem permissão para realizar esta ação", HttpStatus.FORBIDDEN);
    }
}
