package com.italooliveira.projeto.todo_list.dto;

import java.util.UUID;

public record CategoryResponseDTO(
    UUID id,
    String name,
    String color,
    long taskCount
) {}
