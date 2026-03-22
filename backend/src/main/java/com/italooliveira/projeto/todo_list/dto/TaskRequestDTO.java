package com.italooliveira.projeto.todo_list.dto;

import com.italooliveira.projeto.todo_list.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskRequestDTO(
    @NotBlank(message = "O título é obrigatório")
    String title,
    
    String description,
    
    @NotNull(message = "A prioridade é obrigatória")
    Priority priority
) {}