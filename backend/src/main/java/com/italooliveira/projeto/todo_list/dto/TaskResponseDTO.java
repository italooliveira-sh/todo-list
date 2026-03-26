package com.italooliveira.projeto.todo_list.dto;

import com.italooliveira.projeto.todo_list.domain.enums.Priority;
import com.italooliveira.projeto.todo_list.domain.enums.TaskStatus;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

public record TaskResponseDTO(
    UUID id,
    String title,
    String description,
    TaskStatus status,
    String statusDescription,
    Priority priority,
    String priorityDescription,
    LocalDateTime deadline,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {}
