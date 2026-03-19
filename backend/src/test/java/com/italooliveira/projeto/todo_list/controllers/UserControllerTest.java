package com.italooliveira.projeto.todo_list.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.italooliveira.projeto.todo_list.dto.UserRegistrationDTO;
import com.italooliveira.projeto.todo_list.dto.UserResponseDTO;
import com.italooliveira.projeto.todo_list.exceptions.EmailAlreadyExistsException;
import com.italooliveira.projeto.todo_list.exceptions.UsernameAlreadyExistsException;
import com.italooliveira.projeto.todo_list.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve retornar 201 ao registar um utilizador com sucesso")
    void shouldReturnCreatedWhenUserIsRegistered() throws Exception {
        // Arrange
        var dto = new UserRegistrationDTO("italo", "italo@email.com", "senha123");
        var response = new UserResponseDTO(UUID.randomUUID(), "italo", "italo@email.com", OffsetDateTime.now());

        when(userService.registerUser(any(UserRegistrationDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("italo"))
                .andExpect(jsonPath("$.email").value("italo@email.com"))
                .andExpect(jsonPath("$.password").doesNotExist()); // Garante que a senha não é exposta
    }

    @Test
    @DisplayName("Deve retornar 409 quando o GlobalExceptionHandler captura EmailAlreadyExistsException")
    void shouldReturnConflictWhenEmailAlreadyExists() throws Exception {
        // Arrange
        var dto = new UserRegistrationDTO("italo", "duplicado@email.com", "senha123");
        
        when(userService.registerUser(any())).thenThrow(new EmailAlreadyExistsException());

        // Act & Assert
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.statusText").value("Conflict"))
                .andExpect(jsonPath("$.message").value("E-mail já cadastrado no sistema"))
                .andExpect(jsonPath("$.method").value("POST"))
                .andExpect(jsonPath("$.path").value("/api/users"));
    }

    @Test
    @DisplayName("Deve retornar 409 quando o username já existe")
    void shouldReturnConflictWhenUsernameAlreadyExists() throws Exception {
        var dto = new UserRegistrationDTO("italo_duplicado", "outro@email.com", "senha123");
        
        when(userService.registerUser(any())).thenThrow(new UsernameAlreadyExistsException());

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.statusText").value("Conflict"))
                .andExpect(jsonPath("$.message").value("Nome de usuário já cadastrado"))
                .andExpect(jsonPath("$.path").value("/api/users"))
                .andExpect(jsonPath("$.method").value("POST"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("Deve retornar 400 quando os dados de cadastro forem inválidos")
    void shouldReturnBadRequestWhenDataIsInvalid() throws Exception {
        // Arrange: Enviamos dados que violam as anotações @NotBlank e @Email
        var invalidDto = new UserRegistrationDTO("", "email-invalido", "");

        // Act & Assert
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.statusText").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Campos inválidos"))
                .andExpect(jsonPath("$.path").value("/api/users"))
                .andExpect(jsonPath("$.method").value("POST"))
                
                // Validação das mensagens específicas de cada campo no Map 'errors'
                .andExpect(jsonPath("$.errors.username").exists())
                .andExpect(jsonPath("$.errors.email").exists())
                .andExpect(jsonPath("$.errors.password").exists());
    }
}