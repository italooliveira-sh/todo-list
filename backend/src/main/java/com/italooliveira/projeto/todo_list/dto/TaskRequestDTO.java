package com.italooliveira.projeto.todo_list.dto;

import com.italooliveira.projeto.todo_list.domain.enums.Priority;
import jakarta.validation.constraints.FutureOrPresent;
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

    @FutureOrPresent(message = "O prazo não pode ser uma data no passado")
    LocalDateTime deadline,

    UUID categoryId
) {}
