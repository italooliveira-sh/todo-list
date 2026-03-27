package com.italooliveira.projeto.todo_list.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.italooliveira.projeto.todo_list.domain.User;
import com.italooliveira.projeto.todo_list.dto.LoginRequestDTO;
import com.italooliveira.projeto.todo_list.dto.LoginResponseDTO;
import com.italooliveira.projeto.todo_list.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("Deve retornar um token quando as credenciais forem válidas")
    void shouldReturnTokenWhenCredentialsAreValid() {
        // Arrange
        var request = new LoginRequestDTO("italo@email.com", "senha123");
        var user = User.builder()
                .email(request.email())
                .password("senhaCriptografada")
                .build();

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(tokenService.generateToken(user)).thenReturn("token-gerado");
        
        // Act
        LoginResponseDTO response = authService.login(request);

        // Assert
        assertNotNull(response.token());
        assertEquals("token-gerado", response.token());
    }
}
