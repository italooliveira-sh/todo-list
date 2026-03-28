package com.italooliveira.projeto.todo_list.dto;

import com.italooliveira.projeto.todo_list.domain.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public record TaskRequestDTO(
    @NotBlank(message = "O título é obrigatório")
    String title,

    String description,

    @NotNull(message = "A prioridade é obrigatória")
    Priority priority,

    LocalDateTime deadline,

    UUID categoryId
) {}
