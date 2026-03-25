package com.italooliveira.projeto.todo_list.services;

import com.italooliveira.projeto.todo_list.domain.Task;
import com.italooliveira.projeto.todo_list.domain.User;
import com.italooliveira.projeto.todo_list.dto.TaskRequestDTO;
import com.italooliveira.projeto.todo_list.dto.TaskResponseDTO;
import com.italooliveira.projeto.todo_list.exceptions.ForbiddenActionException;
import com.italooliveira.projeto.todo_list.exceptions.ResourceNotFoundException;
import com.italooliveira.projeto.todo_list.mappers.TaskMapper;
import com.italooliveira.projeto.todo_list.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Transactional
    public TaskResponseDTO createTask(TaskRequestDTO dto) {
        User user = getAuthenticatedUser();
        Task task = taskMapper.toEntity(dto, user);
        taskRepository.save(task);
        return taskMapper.toResponseDTO(task);
    }

    @Transactional(readOnly = true)
    public TaskResponseDTO findTaskById(UUID id) {
        User user = getAuthenticatedUser();
        Task task = findByIdOrThrow(id);
        validateOwnership(task, user);
        
        return taskMapper.toResponseDTO(task);
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDTO> findAllTasks() {
        User user = getAuthenticatedUser();
        return taskRepository.findByUserId(user.getId()).stream()
                .map(taskMapper::toResponseDTO).toList();
    }

    @Transactional
    public TaskResponseDTO updateTask(UUID id, TaskRequestDTO dto) {
        User user = getAuthenticatedUser();
        Task task = findByIdOrThrow(id);
        validateOwnership(task, user);

        task.setTitle(dto.title());
        task.setDescription(dto.description());
        task.setPriority(dto.priority());

        return taskMapper.toResponseDTO(task);
    }

    @Transactional
    public void deleteTask(UUID id) {
        User user = getAuthenticatedUser();
        Task task = findByIdOrThrow(id);
        validateOwnership(task, user);
        
        taskRepository.delete(task);
    }

    private Task findByIdOrThrow(UUID id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada"));
    }

    private void validateOwnership(Task task, User user) {
        if (!task.getUser().getId().equals(user.getId())) {
            throw new ForbiddenActionException();
        }
    }

    private User getAuthenticatedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
