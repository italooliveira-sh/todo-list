package com.italooliveira.projeto.todo_list.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Priority {
    LOW("Baixa"),
    MEDIUM("Média"),
    HIGH("Alta");

    private final String description;
}
