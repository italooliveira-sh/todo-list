package com.italooliveira.projeto.todo_list.mappers;

import com.italooliveira.projeto.todo_list.domain.Task;
import com.italooliveira.projeto.todo_list.domain.User;
import com.italooliveira.projeto.todo_list.domain.enums.TaskStatus;
import com.italooliveira.projeto.todo_list.dto.TaskRequestDTO;
import com.italooliveira.projeto.todo_list.dto.TaskResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public Task toEntity(TaskRequestDTO dto, User user) {
        if (dto == null) return null;

        return Task.builder()
                .title(dto.title())
                .description(dto.description())
                .priority(dto.priority())
                .deadline(dto.deadline())
                .status(TaskStatus.PENDING)
                .user(user)
                .build();
    }

    public TaskResponseDTO toResponseDTO(Task task) {
        if (task == null) return null;

        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getStatus().getDescription(),
                task.getPriority(),
                task.getPriority().getDescription(),
                task.getDeadline(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}
