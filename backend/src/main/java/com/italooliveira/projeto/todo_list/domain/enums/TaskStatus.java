package com.italooliveira.projeto.todo_list.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskStatus {
    PENDING("Pendente"),
    DOING("Em Andamento"),
    DONE("Concluída");

    private final String description;
}
