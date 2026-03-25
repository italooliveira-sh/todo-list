package com.italooliveira.projeto.todo_list.services;

import com.italooliveira.projeto.todo_list.domain.Task;
import com.italooliveira.projeto.todo_list.domain.User;
import com.italooliveira.projeto.todo_list.dto.TaskRequestDTO;
import com.italooliveira.projeto.todo_list.dto.TaskResponseDTO;
import com.italooliveira.projeto.todo_list.exceptions.ForbiddenActionException;
import com.italooliveira.projeto.todo_list.exceptions.ResourceNotFoundException;
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
    @DisplayName("Deve buscar uma tarefa por ID com sucesso se o usuário for o dono")
    void shouldFindTaskByIdSuccessfullyWhenUserIsOwner() {
        // Arrange
        var taskId = UUID.randomUUID();
        var user = User.builder().id(UUID.randomUUID()).email("italo@email.com").build();
        var task = Task.builder()
                .id(taskId)
                .title("Estudar Spring Security")
                .user(user)
                .build();

        mockSecurityContext(user);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        // Act
        TaskResponseDTO result = taskService.findTaskById(taskId);

        // Assert
        assertNotNull(result);
        assertEquals("Estudar Spring Security", result.title());
        verify(taskRepository).findById(taskId);
        
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao buscar tarefa inexistente")
    void shouldThrowResourceNotFoundExceptionWhenFindingNonExistentTask() {
        // Arrange
        var idInexistente = UUID.randomUUID();
        var user = User.builder().id(UUID.randomUUID()).build();

        mockSecurityContext(user);

        // Simulamos que o repository retorna VAZIO para esse ID
        when(taskRepository.findById(idInexistente)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.findTaskById(idInexistente);
        });

        // Verificamos que o fluxo parou na busca e não tentou processar mais nada
        verify(taskRepository).findById(idInexistente);
        SecurityContextHolder.clearContext();
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

        when(taskRepository.findById(taskId)).thenReturn(java.util.Optional.of(taskDoDono));

        // Act & Assert
        assertThrows(ForbiddenActionException.class, () -> {
            taskService.findTaskById(taskId);
        });

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

    @Test
    @DisplayName("Deve atualizar uma tarefa com sucesso se o usuário for o dono")
    void shouldUpdateTaskSuccessfullyWhenUserIsOwner() {
        // Arrange
        var taskId = UUID.randomUUID();
        var user = User.builder().id(UUID.randomUUID()).email("italo@email.com").build();
        
        var existingTask = Task.builder()
                .id(taskId)
                .title("Título Antigo")
                .description("Descrição Antiga")
                .priority(Priority.LOW)
                .user(user)
                .build();

        var updateRequest = new TaskRequestDTO("Novo Título", "Nova Descrição", Priority.MEDIUM);

        mockSecurityContext(user);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));

        // Act
        TaskResponseDTO result = taskService.updateTask(taskId, updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Novo Título", result.title());
        assertEquals("Nova Descrição", result.description());
        assertEquals(Priority.MEDIUM, result.priority());
        
        // Verificamos se o objeto 'existingTask' (em memória/mock) foi de fato alterado
        assertEquals("Novo Título", existingTask.getTitle());
        assertEquals(Priority.MEDIUM, existingTask.getPriority());

        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Deve lançar ForbiddenActionException ao tentar atualizar tarefa de outro usuário")
    void shouldThrowForbiddenExceptionWhenUpdatingTaskFromAnotherUser() {
        // Arrange
        var taskId = UUID.randomUUID();
        var donoOriginal = User.builder().id(UUID.randomUUID()).email("dono@email.com").build();
        var usuarioInvasor = User.builder().id(UUID.randomUUID()).email("invasor@email.com").build();
        
        var taskDoDono = Task.builder().id(taskId).user(donoOriginal).build();
        var request = new TaskRequestDTO("Titulo", "Desc", Priority.LOW);

        mockSecurityContext(usuarioInvasor); // Invasor logado

        when(taskRepository.findById(taskId)).thenReturn(java.util.Optional.of(taskDoDono));

        // Act & Assert
        assertThrows(ForbiddenActionException.class, () -> {
            taskService.updateTask(taskId, request);
        });

        verify(taskRepository, never()).save(any());
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao tentar atualizar tarefa inexistente")
    void shouldThrowResourceNotFoundExceptionWhenTaskDoesNotExist() {
        // Arrange
        var idInexistente = UUID.randomUUID();
        var user = User.builder().id(UUID.randomUUID()).build();
        var request = new TaskRequestDTO("Título", "Desc", Priority.LOW);

        mockSecurityContext(user);

        // Simulamos que o banco retornou VAZIO
        when(taskRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.updateTask(idInexistente, request);
        });

        verify(taskRepository, never()).save(any());
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Deve deletar uma tarefa com sucesso se o usuário for o dono")
    void shouldDeleteTaskSuccessfullyWhenUserIsOwner() {
        // Arrange
        var taskId = UUID.randomUUID();
        var user = User.builder().id(UUID.randomUUID()).email("italo@email.com").build();
        var task = Task.builder().id(taskId).user(user).build();

        mockSecurityContext(user);

        when(taskRepository.findById(taskId)).thenReturn(java.util.Optional.of(task));

        // Act
        taskService.deleteTask(taskId);

        // Assert
        verify(taskRepository).delete(task); // Garante que o repository foi chamado
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Deve lançar ForbiddenActionException ao tentar DELETAR tarefa de outro utilizador")
    void shouldThrowForbiddenExceptionWhenDeletingTaskFromAnotherUser() {
        // Arrange
        var taskId = UUID.randomUUID();
        var donoOriginal = User.builder().id(UUID.randomUUID()).email("dono@email.com").build();
        var invasor = User.builder().id(UUID.randomUUID()).email("invasor@email.com").build();
        
        var taskDoDono = Task.builder().id(taskId).user(donoOriginal).build();

        mockSecurityContext(invasor); // Simulamos o invasor logado

        when(taskRepository.findById(taskId)).thenReturn(java.util.Optional.of(taskDoDono));

        // Act & Assert
        assertThrows(ForbiddenActionException.class, () -> {
            taskService.deleteTask(taskId);
        });

        // Garante que o delete NUNCA foi chamado no repository
        verify(taskRepository, never()).delete(any());
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao tentar DELETAR tarefa inexistente")
    void shouldThrowResourceNotFoundExceptionWhenDeletingNonExistentTask() {
        // Arrange
        var idInexistente = UUID.randomUUID();
        var user = User.builder().id(UUID.randomUUID()).build();

        mockSecurityContext(user);

        when(taskRepository.findById(idInexistente)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.deleteTask(idInexistente);
        });

        verify(taskRepository, never()).delete(any());
        SecurityContextHolder.clearContext();
    }
}
