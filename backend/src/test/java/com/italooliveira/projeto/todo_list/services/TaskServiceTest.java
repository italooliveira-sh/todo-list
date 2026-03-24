package com.italooliveira.projeto.todo_list.services;

import com.italooliveira.projeto.todo_list.domain.Task;
import com.italooliveira.projeto.todo_list.domain.User;
import com.italooliveira.projeto.todo_list.dto.TaskRequestDTO;
import com.italooliveira.projeto.todo_list.dto.TaskResponseDTO;
import com.italooliveira.projeto.todo_list.domain.enums.Priority;
import com.italooliveira.projeto.todo_list.mappers.TaskMapper;
import com.italooliveira.projeto.todo_list.repositories.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Spy
    private TaskMapper taskMapper = new TaskMapper();

    @InjectMocks
    private TaskService taskService;

    private void mockSecurityContext(User user) {
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(auth.getPrincipal()).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("Deve criar uma tarefa associada ao usuário autenticado no contexto")
    void shouldCreateTaskSuccessfullyWithAuthenticatedUser() {
        // Arrange
        var user = User.builder().id(UUID.randomUUID()).email("italo@email.com").build();
        var request = new TaskRequestDTO("Estudar TDD", "Finalizar módulo de Tasks", Priority.HIGH);

        mockSecurityContext(user);

        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task task = invocation.getArgument(0);
            task.setId(UUID.randomUUID());
            return task;
        });

        // Act
        TaskResponseDTO result = taskService.createTask(request);

        // Assert
        assertNotNull(result);
        assertEquals(request.title(), result.title());
        verify(taskRepository).save(argThat(task -> task.getUser().equals(user)));
        
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Deve listar todas as tarefas do usuário autenticado")
    void shouldListAllTasksForAuthenticatedUser() {
        // Arrange
        var user = User.builder().id(UUID.randomUUID()).email("italo@email.com").build();
        var task1 = Task.builder().id(UUID.randomUUID()).title("Task 1").user(user).build();
        var task2 = Task.builder().id(UUID.randomUUID()).title("Task 2").user(user).build();
        
        mockSecurityContext(user);

        when(taskRepository.findByUserId(user.getId())).thenReturn(List.of(task1, task2));

        // Act
        List<TaskResponseDTO> result = taskService.findAllTasks();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Task 1", result.get(0).title());
        assertEquals("Task 2", result.get(1).title());
        
        verify(taskRepository).findByUserId(user.getId());
        SecurityContextHolder.clearContext();
    }
}
