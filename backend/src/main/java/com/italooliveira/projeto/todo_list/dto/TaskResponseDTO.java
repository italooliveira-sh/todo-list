package com.italooliveira.projeto.todo_list.dto;

import com.italooliveira.projeto.todo_list.enums.Priority;
import com.italooliveira.projeto.todo_list.enums.TaskStatus;
import java.time.OffsetDateTime;
import java.util.UUID;

public record TaskResponseDTO(
    UUID id,
    String title,
    String description,
    Priority priority,
    TaskStatus status,
    OffsetDateTime createdAt
) {}