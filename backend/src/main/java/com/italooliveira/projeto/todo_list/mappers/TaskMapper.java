package com.italooliveira.projeto.todo_list.mappers;

import com.italooliveira.projeto.todo_list.domain.Task;
import com.italooliveira.projeto.todo_list.domain.User;
import com.italooliveira.projeto.todo_list.dto.TaskRequestDTO;
import com.italooliveira.projeto.todo_list.dto.TaskResponseDTO;
import com.italooliveira.projeto.todo_list.domain.enums.TaskStatus;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public Task toEntity(TaskRequestDTO dto, User user) {
        return Task.builder()
                .title(dto.title())
                .description(dto.description())
                .priority(dto.priority())
                .status(TaskStatus.PENDING) // Regra de negócio: Status inicial
                .user(user)
                .build();
    }

    public TaskResponseDTO toResponseDTO(Task task) {
        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getPriority(),
                task.getStatus(),
                task.getCreatedAt()
        );
    }
}