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
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Task task = taskMapper.toEntity(dto, user);
        Task savedTask = taskRepository.save(task);
        return taskMapper.toResponseDTO(savedTask);
    }

    public List<TaskResponseDTO> findAllTasks() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return taskRepository.findByUserId(user.getId()).stream()
                .map(taskMapper::toResponseDTO).toList();
    }

    @Transactional
    public TaskResponseDTO updateTask(UUID id, TaskRequestDTO dto) {
        // 1. Recupera o usuário logado do contexto de segurança
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 2. Busca a tarefa existente ou lança exceção se não encontrar
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada"));

        // 3. Validação de segurança: verifica se o usuário logado é o dono da tarefa
        if (!task.getUser().getId().equals(currentUser.getId())) {
            throw new ForbiddenActionException();
        }

        // 4. Atualiza os campos da entidade com os dados do DTO
        task.setTitle(dto.title());
        task.setDescription(dto.description());
        task.setPriority(dto.priority());

        // 5. Salva e retorna o DTO de resposta mapeado
        return taskMapper.toResponseDTO(taskRepository.save(task));
    }

}