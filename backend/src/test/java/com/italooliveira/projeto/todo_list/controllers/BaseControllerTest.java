package com.italooliveira.projeto.todo_list.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.italooliveira.projeto.todo_list.repositories.UserRepository;
import com.italooliveira.projeto.todo_list.services.TokenService;
import com.italooliveira.projeto.todo_list.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc(addFilters = false)
public abstract class BaseControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    // Mocks exigidos pela SecurityConfig para o contexto subir
    @MockitoBean
    protected TokenService tokenService;

    @MockitoBean
    protected UserRepository userRepository;

    @MockitoBean
    protected UserMapper userMapper;
}