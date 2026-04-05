package com.italooliveira.projeto.todo_list.exceptions;

import org.springframework.http.HttpStatus;

public class TaskNotStartedException extends BusinessException {
    public TaskNotStartedException() {
        super("A tarefa precisa ser iniciada antes de ser concluída", HttpStatus.BAD_REQUEST);
    }
}
