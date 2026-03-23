package com.italooliveira.projeto.todo_list.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserResponseDTO(
    UUID id,
    String name,
    String email,
    OffsetDateTime createdAt
) {}