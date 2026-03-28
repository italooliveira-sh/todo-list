package com.italooliveira.projeto.todo_list.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.italooliveira.projeto.todo_list.domain.enums.Priority;
import com.italooliveira.projeto.todo_list.domain.enums.TaskStatus;
import com.italooliveira.projeto.todo_list.dto.TaskRequestDTO;
import com.italooliveira.projeto.todo_list.dto.TaskResponseDTO;
import com.italooliveira.projeto.todo_list.exceptions.ForbiddenActionException;
import com.italooliveira.projeto.todo_list.exceptions.ResourceNotFoundException;
import com.italooliveira.projeto.todo_list.exceptions.TaskAlreadyStartedException;
import com.italooliveira.projeto.todo_list.services.TaskService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TaskControllerTest extends BaseControllerTest{

    @MockitoBean
    private TaskService taskService;

    @Test
    @DisplayName("Deve retornar 201 ao criar uma tarefa com sucesso")
    void shouldReturn201WhenCreatingTaskSuccessfully() throws Exception {
        // Arrange
        var taskId = UUID.randomUUID();
        var now = OffsetDateTime.now();
        var deadline = LocalDateTime.now().plusDays(2);
        
        var request = new TaskRequestDTO(
            "Finalizar API", 
            "Implementar testes de controller", 
            Priority.HIGH,
            deadline,
            null
        );

        var response = new TaskResponseDTO(
            taskId,
            request.title(),
            request.description(),
            TaskStatus.PENDING,
            TaskStatus.PENDING.getDescription(),
            Priority.HIGH,
            Priority.HIGH.getDescription(),
            deadline,
            null,
            now, 
            now
        );

        when(taskService.createTask(any(TaskRequestDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(taskId.toString()))
                .andExpect(jsonPath("$.statusDescription").value("Pendente"))
                .andExpect(jsonPath("$.priorityDescription").value("Alta"))
                // Validando que o deadline está presente
                .andExpect(jsonPath("$.deadline").exists());
    }

    @Test
    @DisplayName("Deve retornar 200 ao buscar uma tarefa por ID")
    void shouldReturn200WhenFindingTaskById() throws Exception {
        var taskId = UUID.randomUUID();
        var response = new TaskResponseDTO(
            taskId,
            "Task", 
            "Desc", 
            TaskStatus.PENDING, 
            "Pendente", 
            Priority.LOW, 
            "Baixa", 
            LocalDateTime.now(), 
            null,
            OffsetDateTime.now(), 
            OffsetDateTime.now());

        when(taskService.findTaskById(taskId)).thenReturn(response);

        mockMvc.perform(get("/api/tasks/{id}", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId.toString()))
                .andExpect(jsonPath("$.title").value("Task"));
    }

    @Test
    @DisplayName("Deve retornar 404 quando a tarefa não existe")
    void shouldReturn404WhenTaskNotFound() throws Exception {
        var taskId = UUID.randomUUID();
        
        // Simulamos o service lançando a sua exceção customizada
        when(taskService.findTaskById(taskId)).thenThrow(new ResourceNotFoundException("Tarefa não encontrada"));

        mockMvc.perform(get("/api/tasks/{id}", taskId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 403 quando o usuário não é o dono")
    void shouldReturn403WhenUserIsNotOwner() throws Exception {
        var taskId = UUID.randomUUID();
        
        when(taskService.findTaskById(taskId)).thenThrow(new ForbiddenActionException());

        mockMvc.perform(get("/api/tasks/{id}", taskId))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve retornar 200 ao buscar todas as tarefas do usuário")
    void shouldReturn200WhenFindingAllTasks() throws Exception {
        // Arrange
        var taskId1 = UUID.randomUUID();
        var taskId2 = UUID.randomUUID();
        var now = OffsetDateTime.now();

        var task1 = new TaskResponseDTO(taskId1, "Task 1", "Desc 1", TaskStatus.PENDING, "Pendente", Priority.LOW, "Baixa", LocalDateTime.now(), null, now, now);
        var task2 = new TaskResponseDTO(taskId2, "Task 2", "Desc 2", TaskStatus.DOING, "Em Andamento", Priority.HIGH, "Alta", LocalDateTime.now(), null, now, now);

        var taskList = List.of(task1, task2);

        when(taskService.findAllTasks()).thenReturn(taskList);

        // Act & Assert
        mockMvc.perform(get("/api/tasks")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(taskId1.toString()))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[1].id").value(taskId2.toString()))
                .andExpect(jsonPath("$[1].status").value("DOING"));
    }

    @Test
    @DisplayName("Deve retornar 400 ao tentar criar tarefa com dados inválidos")
    void shouldReturn400WhenCreatingWithInvalidData() throws Exception {
        var invalidRequest = new TaskRequestDTO("", "", null, null, null);

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Campos inválidos"))
                .andExpect(jsonPath("$.errors.title").value("O título é obrigatório"))
                .andExpect(jsonPath("$.errors.priority").value("A prioridade é obrigatória"));
    }

    @Test
    @DisplayName("Deve retornar 200 ao atualizar uma tarefa com sucesso")
    void shouldReturn200WhenUpdatingTask() throws Exception {
        var taskId = UUID.randomUUID();
        var deadline = LocalDateTime.now().plusDays(5);
        var now = OffsetDateTime.now();

        var request = new TaskRequestDTO("Título Atualizado", "Nova Desc", Priority.MEDIUM, deadline, null);
        
        var response = new TaskResponseDTO(
                taskId, request.title(), request.description(),
                TaskStatus.PENDING, "Pendente",
                Priority.MEDIUM, "Média",
                deadline, null, now, now
        );

        when(taskService.updateTask(eq(taskId), any(TaskRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/tasks/{id}", taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Título Atualizado"))
                .andExpect(jsonPath("$.priorityDescription").value("Média"));
    }

    @Test
    @DisplayName("Deve retornar 400 ao tentar atualizar com dados inválidos")
    void shouldReturn400WhenUpdatingWithInvalidData() throws Exception {
        var taskId = UUID.randomUUID();
        var invalidRequest = new TaskRequestDTO("", "", null, null, null);

        mockMvc.perform(put("/api/tasks/{id}", taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Campos inválidos"))
                .andExpect(jsonPath("$.errors.title").value("O título é obrigatório"))
                .andExpect(jsonPath("$.errors.priority").value("A prioridade é obrigatória"));
    }

    @Test
    @DisplayName("Deve retornar 204 ao deletar uma tarefa com sucesso")
    void shouldReturn204WhenDeletingTask() throws Exception {
        var taskId = UUID.randomUUID();

        // No delete, o service é void, então apenas simulamos que ele não faz nada (doNothing)
        // O Mockito faz isso por padrão para métodos void.

        mockMvc.perform(delete("/api/tasks/{id}", taskId))
                .andExpect(status().isNoContent()); // Esperamos 204

        verify(taskService).deleteTask(taskId); // Garante que o service foi chamado
    }

    @Test
    @DisplayName("Deve retornar 404 ao tentar deletar tarefa inexistente")
    void shouldReturn404WhenDeletingNonExistentTask() throws Exception {
        var taskId = UUID.randomUUID();
        
        // Forçamos o service a lançar a exceção de recurso não encontrado
        doThrow(new ResourceNotFoundException("Tarefa não encontrada"))
            .when(taskService).deleteTask(taskId);

        mockMvc.perform(delete("/api/tasks/{id}", taskId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 200 ao marcar uma tarefa como concluída via PATCH")
    void shouldReturn200WhenMarkingTaskAsDone() throws Exception {
        // Arrange
        var taskId = UUID.randomUUID();
        var now = OffsetDateTime.now();
        
        // Simulamos o retorno do Service com o status DONE e a descrição "Concluída"
        var response = new TaskResponseDTO(
                taskId, "Tarefa Finalizada", "Descrição",
                TaskStatus.DONE, "Concluída", // Aqui validamos sua nova lógica de Enum
                Priority.LOW, "Baixa",
                LocalDateTime.now().plusDays(1), null, now, now
        );

        when(taskService.completeTask(taskId)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(patch("/api/tasks/{id}/done", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DONE"))
                .andExpect(jsonPath("$.statusDescription").value("Concluída"));

        verify(taskService).completeTask(taskId);
    }

    @Test
    @DisplayName("Deve retornar 200 ao iniciar uma tarefa (PENDING -> DOING)")
    void shouldReturn200WhenStartingTask() throws Exception {
        var taskId = UUID.randomUUID();
        var now = OffsetDateTime.now();
        
        // Simulação do retorno com status DOING
        var response = new TaskResponseDTO(
                taskId, "Tarefa em Foco", "Descrição",
                TaskStatus.DOING, "Em Andamento", 
                Priority.MEDIUM, "Média",
                LocalDateTime.now().plusDays(1), null, now, now
        );

        when(taskService.startTask(taskId)).thenReturn(response);

        mockMvc.perform(patch("/api/tasks/{id}/start", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DOING"))
                .andExpect(jsonPath("$.statusDescription").value("Em Andamento"));
    }

    @Test
    @DisplayName("Deve retornar erro de negócio quando a tarefa já foi iniciada")
    void shouldReturnBadRequestWhenTaskAlreadyStarted() throws Exception {
        var taskId = UUID.randomUUID();
        
        // Simulamos o service lançando a sua nova exceção customizada
        when(taskService.startTask(taskId)).thenThrow(new TaskAlreadyStartedException());

        mockMvc.perform(patch("/api/tasks/{id}/start", taskId))
                .andExpect(status().isBadRequest());
    }
}
