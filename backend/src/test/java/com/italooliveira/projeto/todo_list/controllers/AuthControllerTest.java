package com.italooliveira.projeto.todo_list.controllers;

import com.italooliveira.projeto.todo_list.dto.LoginRequestDTO;
import com.italooliveira.projeto.todo_list.dto.LoginResponseDTO;
import com.italooliveira.projeto.todo_list.exceptions.InvalidCredentialsException;
import com.italooliveira.projeto.todo_list.services.AuthService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest extends BaseControllerTest{

    @MockitoBean
    private AuthService authService;

    @Test
    @DisplayName("Deve realizar login com sucesso e retornar um token")
    void shouldLoginSuccessfully() throws Exception {
        // Arrange
        var request = new LoginRequestDTO("italo@email.com", "senha123");
        var responseDTO = new LoginResponseDTO("token-jwt-fake");

        when(authService.login(any(LoginRequestDTO.class))).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token-jwt-fake"));
    }

    @Test
    @DisplayName("Deve retornar 401 e ApiErrorMessage quando as credenciais forem inválidas")
    void shouldReturn401WhenCredentialsAreInvalid() throws Exception {
        var request = new LoginRequestDTO("usuario@errado.com", "senhaIncorreta");

        when(authService.login(any(LoginRequestDTO.class)))
                .thenThrow(new InvalidCredentialsException());

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                // Validando se o JSON de erro tem o campo que você definiu no ApiErrorMessage
                .andExpect(jsonPath("$.message").value("Credenciais inválidas"))
                .andExpect(jsonPath("$.status").value(401));
    }
}