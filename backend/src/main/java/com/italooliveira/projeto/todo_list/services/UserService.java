package com.italooliveira.projeto.todo_list.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.italooliveira.projeto.todo_list.domain.User;
import com.italooliveira.projeto.todo_list.dto.UserRegistrationDTO;
import com.italooliveira.projeto.todo_list.dto.UserResponseDTO;
import com.italooliveira.projeto.todo_list.exceptions.EmailAlreadyExistsException;
import com.italooliveira.projeto.todo_list.exceptions.UsernameAlreadyExistsException;
import com.italooliveira.projeto.todo_list.mappers.UserMapper;
import com.italooliveira.projeto.todo_list.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CategoryService categoryService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional
    public UserResponseDTO registerUser(UserRegistrationDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException();
        }

        User user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.password()));

        User savedUser = userRepository.save(user);

        categoryService.createDefaultCategories(savedUser);

        return userMapper.toResponseDTO(savedUser);
    }
}
