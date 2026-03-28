package com.italooliveira.projeto.todo_list.services;

import com.italooliveira.projeto.todo_list.domain.User;
import com.italooliveira.projeto.todo_list.dto.UserRegistrationDTO;
import com.italooliveira.projeto.todo_list.dto.UserResponseDTO;
import com.italooliveira.projeto.todo_list.exceptions.EmailAlreadyExistsException;
import com.italooliveira.projeto.todo_list.exceptions.UsernameAlreadyExistsException;
import com.italooliveira.projeto.todo_list.mappers.UserMapper;
import com.italooliveira.projeto.todo_list.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CategoryService categoryService;

    @Spy
    private UserMapper userMapper = new UserMapper();

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Deve cadastrar um usuário com sucesso quando os dados forem válidos")
    void shouldRegisterUserSuccessfully() {
        UserRegistrationDTO dto = new UserRegistrationDTO("italo", "italo@email.com", "senha123");
        String senhaCriptografada = "senhaCriptografada";

        User savedUser = User.builder()
                .id(UUID.randomUUID())
                .name(dto.name())
                .email(dto.email())
                .password(senhaCriptografada)
                .build();

        when(passwordEncoder.encode(dto.password())).thenReturn(senhaCriptografada);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponseDTO result = userService.registerUser(dto);

        assertNotNull(result);
        assertEquals(dto.email(), result.email());
        assertEquals(dto.name(), result.name());
        
        verify(passwordEncoder).encode(dto.password());
        verify(userRepository).save(any(User.class));
        verify(categoryService).createDefaultCategories(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o e-mail já estiver cadastrado")
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        UserRegistrationDTO dto = new UserRegistrationDTO("italo", "duplicado@email.com", "senha123");
        
        when(userRepository.existsByEmail(dto.email())).thenReturn(true);

        RuntimeException exception = assertThrows(EmailAlreadyExistsException.class, () -> {
            userService.registerUser(dto);
        });

        assertEquals("E-mail já cadastrado no sistema", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o username já estiver cadastrado")
    void shouldThrowExceptionWhenUsernameAlreadyExists() {
        UserRegistrationDTO dto = new UserRegistrationDTO("italo_duplicado", "outro@email.com", "senha123");
        
        when(userRepository.existsByEmail(dto.email())).thenReturn(false);
        when(userRepository.existsByName(dto.name())).thenReturn(true);

        RuntimeException exception = assertThrows(UsernameAlreadyExistsException.class, () -> {
            userService.registerUser(dto);
        });

        assertEquals("Nome de usuário já cadastrado", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}