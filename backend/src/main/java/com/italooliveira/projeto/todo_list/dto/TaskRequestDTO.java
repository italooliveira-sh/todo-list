package com.italooliveira.projeto.todo_list.dto;

import com.italooliveira.projeto.todo_list.domain.enums.Priority;
import java.time.LocalDateTime;

public record TaskRequestDTO(
    String title,
    String description,
    Priority priority,
    LocalDateTime deadline
) {}
