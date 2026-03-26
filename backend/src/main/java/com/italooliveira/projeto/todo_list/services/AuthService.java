package com.italooliveira.projeto.todo_list.services;

import com.italooliveira.projeto.todo_list.domain.User;
import com.italooliveira.projeto.todo_list.dto.LoginRequestDTO;
import com.italooliveira.projeto.todo_list.dto.LoginResponseDTO;
import com.italooliveira.projeto.todo_list.exceptions.InvalidCredentialsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public LoginResponseDTO login(LoginRequestDTO data) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
            var auth = this.authenticationManager.authenticate(usernamePassword);

            String token = tokenService.generateToken((User) auth.getPrincipal());
            return new LoginResponseDTO(token);
        } catch (Exception e) {
            throw new InvalidCredentialsException();
        }
    }
}