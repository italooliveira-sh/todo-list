package com.italooliveira.projeto.todo_list.services;

import com.italooliveira.projeto.todo_list.dto.LoginRequestDTO;
import com.italooliveira.projeto.todo_list.dto.LoginResponseDTO;
import com.italooliveira.projeto.todo_list.exceptions.InvalidCredentialsException;
import com.italooliveira.projeto.todo_list.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public LoginResponseDTO login(LoginRequestDTO data) {
        var user = userRepository.findByEmail(data.email())
                .orElseThrow(() -> new InvalidCredentialsException());

        if (!passwordEncoder.matches(data.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String token = tokenService.generateToken(user);
        return new LoginResponseDTO(token);
    }
}