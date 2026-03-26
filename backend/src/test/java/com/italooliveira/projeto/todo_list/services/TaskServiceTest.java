package com.italooliveira.projeto.todo_list.services;

import com.italooliveira.projeto.todo_list.domain.Task;
import com.italooliveira.projeto.todo_list.domain.User;
import com.italooliveira.projeto.todo_list.dto.TaskRequestDTO;
import com.italooliveira.projeto.todo_list.dto.TaskResponseDTO;
import com.italooliveira.projeto.todo_list.exceptions.ForbiddenActionException;
import com.italooliveira.projeto.todo_list.exceptions.ResourceNotFoundException;
import com.italooliveira.projeto.todo_list.exceptions.TaskAlreadyStartedException;
import com.italooliveira.projeto.todo_list.domain.enums.Priority;
import com.italooliveira.projeto.todo_list.domain.enums.TaskStatus;
import com.italooliveira.projeto.todo_list.mappers.TaskMapper;
import com.italooliveira.projeto.todo_list.repositories.TaskRepository;
import org.junit.jupiter.api.AfterEach;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

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
        var deadline = LocalDateTime.now().plusDays(1);
        var request = new TaskRequestDTO("Estudar TDD", "Finalizar módulo de Tasks", Priority.HIGH, deadline);

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
        assertEquals(deadline, result.deadline());
        verify(taskRepository).save(argThat(task -> task.getUser().equals(user)));
    }

    @Test
    @DisplayName("Deve buscar uma tarefa por ID com sucesso se o usuário for o dono")
    void shouldFindTaskByIdSuccessfullyWhenUserIsOwner() {
        // Arrange
        var taskId = UUID.randomUUID();
        var user = User.builder().id(UUID.randomUUID()).email("italo@email.com").build();
        var task = Task.builder()
                .id(taskId)
                .title("Estudar Spring Security")
                .status(TaskStatus.PENDING)
                .priority(Priority.MEDIUM)
                .user(user)
                .build();

        mockSecurityContext(user);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        // Act
        TaskResponseDTO result = taskService.findTaskById(taskId);

        // Assert
        assertNotNull(result);
        assertEquals("Estudar Spring Security", result.title());
        assertEquals("Pendente", result.statusDescription());
        verify(taskRepository).findById(taskId);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao buscar tarefa inexistente")
    void shouldThrowResourceNotFoundExceptionWhenFindingNonExistentTask() {
        // Arrange
        var idInexistente = UUID.randomUUID();
        var user = User.builder().id(UUID.randomUUID()).build();

        mockSecurityContext(user);

        when(taskRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.findTaskById(idInexistente);
        });
    }

    @Test
    @DisplayName("Deve lançar ForbiddenActionException ao buscar tarefa de outro usuário")
    void shouldThrowForbiddenExceptionWhenFindingTaskFromAnotherUser() {
        // Arrange
        var taskId = UUID.randomUUID();
        var donoOriginal = User.builder().id(UUID.randomUUID()).build();
        var invasor = User.builder().id(UUID.randomUUID()).build();
        
        var taskDoDono = Task.builder().id(taskId).user(donoOriginal).build();

        mockSecurityContext(invasor);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskDoDono));

        // Act & Assert
        assertThrows(ForbiddenActionException.class, () -> {
            taskService.findTaskById(taskId);
        });
    }

    @Test
    @DisplayName("Deve listar todas as tarefas do usuário autenticado")
    void shouldListAllTasksForAuthenticatedUser() {
        // Arrange
        var user = User.builder().id(UUID.randomUUID()).email("italo@email.com").build();
        var task1 = Task.builder()
                .id(UUID.randomUUID())
                .title("Task 1")
                .status(TaskStatus.PENDING)
                .priority(Priority.LOW)
                .user(user)
                .build();
        var task2 = Task.builder()
                .id(UUID.randomUUID())
                .title("Task 2")
                .status(TaskStatus.DONE)
                .priority(Priority.HIGH)
                .user(user)
                .build();
        
        mockSecurityContext(user);

        when(taskRepository.findByUserId(user.getId())).thenReturn(List.of(task1, task2));

        // Act
        List<TaskResponseDTO> result = taskService.findAllTasks();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(taskRepository).findByUserId(user.getId());
    }

    @Test
    @DisplayName("Deve atualizar uma tarefa com sucesso se o usuário for o dono")
    void shouldUpdateTaskSuccessfullyWhenUserIsOwner() {
        // Arrange
        var taskId = UUID.randomUUID();
        var user = User.builder().id(UUID.randomUUID()).email("italo@email.com").build();
        var deadline = LocalDateTime.now().plusDays(2);
        
        var existingTask = Task.builder()
                .id(taskId)
                .title("Título Antigo")
                .status(TaskStatus.PENDING)
                .priority(Priority.LOW)
                .user(user)
                .build();

        var updateRequest = new TaskRequestDTO("Novo Título", "Nova Descrição", Priority.MEDIUM, deadline);

        mockSecurityContext(user);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));

        // Act
        TaskResponseDTO result = taskService.updateTask(taskId, updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Novo Título", result.title());
        assertEquals("Média", result.priorityDescription());
        assertEquals("Novo Título", existingTask.getTitle());
    }

    @Test
    @DisplayName("Deve deletar uma tarefa com sucesso se o usuário for o dono")
    void shouldDeleteTaskSuccessfullyWhenUserIsOwner() {
        // Arrange
        var taskId = UUID.randomUUID();
        var user = User.builder().id(UUID.randomUUID()).email("italo@email.com").build();
        var task = Task.builder().id(taskId).user(user).build();

        mockSecurityContext(user);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        // Act
        taskService.deleteTask(taskId);

        // Assert
        verify(taskRepository).delete(task);
    }

    @Test
    @DisplayName("Deve lançar ForbiddenActionException ao deletar tarefa de outro usuário")
    void shouldThrowForbiddenExceptionWhenDeletingTaskFromAnotherUser() {
        // Arrange
        var taskId = UUID.randomUUID();
        var donoOriginal = User.builder().id(UUID.randomUUID()).build();
        var invasor = User.builder().id(UUID.randomUUID()).build();
        
        var taskDoDono = Task.builder().id(taskId).user(donoOriginal).build();

        mockSecurityContext(invasor);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskDoDono));

        // Act & Assert
        assertThrows(ForbiddenActionException.class, () -> {
            taskService.deleteTask(taskId);
        });

        verify(taskRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao deletar tarefa inexistente")
    void shouldThrowResourceNotFoundExceptionWhenDeletingNonExistentTask() {
        // Arrange
        var idInexistente = UUID.randomUUID();
        var user = User.builder().id(UUID.randomUUID()).build();

        mockSecurityContext(user);

        when(taskRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.deleteTask(idInexistente);
        });
    }

    @Test
    @DisplayName("Deve marcar uma tarefa como concluída (DONE) com sucesso")
    void shouldCompleteTaskSuccessfully() {
        // Arrange
        var taskId = UUID.randomUUID();
        var user = User.builder().id(UUID.randomUUID()).email("italo@email.com").build();
        
        var task = Task.builder()
                .id(taskId)
                .title("Finalizar CRUD")
                .status(TaskStatus.PENDING)
                .priority(Priority.LOW)
                .user(user)
                .build();

        mockSecurityContext(user);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        // Act
        TaskResponseDTO result = taskService.completeTask(taskId);

        // Assert
        assertNotNull(result);
        assertEquals(TaskStatus.DONE, result.status());
        assertEquals("Concluída", result.statusDescription());
        assertEquals(TaskStatus.DONE, task.getStatus()); // Verifica o Dirty Checking
    }

    @Test
    @DisplayName("Deve mudar status para DOING com sucesso")
    void shouldStartTaskSuccessfully() {
        var taskId = UUID.randomUUID();
        var user = User.builder().id(UUID.randomUUID()).build();
        var task = Task.builder()
            .id(taskId)
            .user(user)
            .status(TaskStatus.PENDING)
            .priority(Priority.LOW)
            .build();

        mockSecurityContext(user);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        var result = taskService.startTask(taskId);

        assertEquals(TaskStatus.DOING, result.status());
        assertEquals("Em Andamento", result.statusDescription());
        assertEquals(TaskStatus.DOING, task.getStatus());
    }

    @Test
    @DisplayName("Deve lançar TaskAlreadyStartedException ao tentar iniciar tarefa que não está pendente")
    void shouldThrowExceptionWhenTaskIsNotPending() {
        var taskId = UUID.randomUUID();
        var user = User.builder().id(UUID.randomUUID()).build();
        var task = Task.builder()
            .id(taskId)
            .user(user)
            .status(TaskStatus.DOING)
            .build();

        mockSecurityContext(user);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        assertThrows(TaskAlreadyStartedException.class, () -> taskService.startTask(taskId));
    }
}
