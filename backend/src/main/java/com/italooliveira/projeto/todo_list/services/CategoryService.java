package com.italooliveira.projeto.todo_list.services;

import com.italooliveira.projeto.todo_list.domain.Category;
import com.italooliveira.projeto.todo_list.domain.User;
import com.italooliveira.projeto.todo_list.dto.CategoryRequestDTO;
import com.italooliveira.projeto.todo_list.dto.CategoryResponseDTO;
import com.italooliveira.projeto.todo_list.exceptions.CategoryAlreadyExistsException;
import com.italooliveira.projeto.todo_list.exceptions.ForbiddenActionException;
import com.italooliveira.projeto.todo_list.exceptions.ResourceNotFoundException;
import com.italooliveira.projeto.todo_list.mappers.CategoryMapper;
import com.italooliveira.projeto.todo_list.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    public CategoryResponseDTO createCategory(CategoryRequestDTO dto) {
        User user = getAuthenticatedUser();

        if (categoryRepository.existsByNameAndUserId(dto.name(), user.getId())) {
            throw new CategoryAlreadyExistsException();
        }

        Category category = categoryMapper.toEntity(dto, user);
        categoryRepository.save(category);
        return categoryMapper.toResponseDTO(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> findAllCategories() {
        User user = getAuthenticatedUser();
        return categoryRepository.findByUserId(user.getId()).stream()
                .map(categoryMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public CategoryResponseDTO updateCategory(UUID id, CategoryRequestDTO dto) {
        User user = getAuthenticatedUser();
        Category category = findByIdOrThrow(id);
        validateOwnership(category, user);

        category.setName(dto.name());
        category.setColor(dto.color());

        return categoryMapper.toResponseDTO(category);
    }

    @Transactional
    public void deleteCategory(UUID id) {
        User user = getAuthenticatedUser();
        Category category = findByIdOrThrow(id);
        validateOwnership(category, user);
        categoryRepository.delete(category);
    }

    @Transactional
    public void createDefaultCategories(User user) {
        List<Category> defaults = List.of(
            Category.builder().name("Geral").color("#808080").user(user).build(),
            Category.builder().name("Trabalho").color("#4169E1").user(user).build(),
            Category.builder().name("Pessoal").color("#32CD32").user(user).build(),
            Category.builder().name("Estudos").color("#FFD700").user(user).build()
        );
        categoryRepository.saveAll(defaults);
    }

    public Category findByIdOrThrow(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
    }

    private void validateOwnership(Category category, User user) {
        if (!category.getUser().getId().equals(user.getId())) {
            throw new ForbiddenActionException();
        }
    }

    private User getAuthenticatedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
