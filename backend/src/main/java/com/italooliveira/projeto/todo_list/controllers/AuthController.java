package com.italooliveira.projeto.todo_list.controllers;

import com.italooliveira.projeto.todo_list.dto.LoginRequestDTO;
import com.italooliveira.projeto.todo_list.dto.LoginResponseDTO;
import com.italooliveira.projeto.todo_list.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO data) {
        var response = authService.login(data);
        return ResponseEntity.ok(response);
    }
}