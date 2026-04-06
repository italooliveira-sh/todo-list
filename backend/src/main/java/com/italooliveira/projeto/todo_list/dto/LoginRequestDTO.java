package com.italooliveira.projeto.todo_list.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginRequestDTO(
    @NotBlank(message = "O e-mail é obrigatório")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "O e-mail informado é inválido")
    String email,
    
    @NotBlank(message = "A senha é obrigatória")
    String password
) {}
