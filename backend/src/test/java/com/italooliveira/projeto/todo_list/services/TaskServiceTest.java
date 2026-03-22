package com.italooliveira.projeto.todo_list.services;

import com.italooliveira.projeto.todo_list.domain.Task;
import com.italooliveira.projeto.todo_list.domain.User;
import com.italooliveira.projeto.todo_list.dto.TaskRequestDTO;
import com.italooliveira.projeto.todo_list.dto.TaskResponseDTO;
import com.italooliveira.projeto.todo_list.enums.Priority;
import com.italooliveira.projeto.todo_list.enums.TaskStatus;
import com.italooliveira.projeto.todo_list.mappers.TaskMapper;
import com.italooliveira.projeto.todo_list.repositories.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Spy
    private TaskMapper taskMapper = new TaskMapper(); // Usamos Spy para testar a conversão real

    @InjectMocks
    private TaskService taskService;

    @Test
    @DisplayName("Deve criar uma tarefa com sucesso para um usuário específico")
    void shouldCreateTaskSuccessfully() {
        // Arrange
        var user = User.builder().id(UUID.randomUUID()).username("italo").build();
        var request = new TaskRequestDTO("Estudar TDD", "Finalizar módulo de Tasks", Priority.HIGH);
        
        // Simulamos o comportamento do repository retornando a task que o mapper geraria
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task task = invocation.getArgument(0);
            task.setId(UUID.randomUUID()); // Simula o ID gerado pelo banco
            return task;
        });

        // Act
        TaskResponseDTO result = taskService.createTask(request, user);

        // Assert
        assertNotNull(result);
        assertEquals(request.title(), result.title());
        assertEquals(TaskStatus.PENDING, result.status());
        verify(taskRepository).save(any(Task.class));
    }
}