package com.italooliveira.projeto.todo_list.exceptions;

import org.springframework.http.HttpStatus;

public class TaskAlreadyStartedException extends BusinessException {
    public TaskAlreadyStartedException() {
        super("A tarefa já está em andamento ou já foi concluida.", HttpStatus.BAD_REQUEST);
    }
    
}
