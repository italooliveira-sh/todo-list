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
import com.italooliveira.projeto.todo_list.repositories.TaskRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TaskRepository taskRepository;

    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        categoryMapper = new CategoryMapper(taskRepository);
        categoryService = new CategoryService(categoryRepository, categoryMapper);
    }

    private void mockSecurityContext(User user) {
        var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    @DisplayName("Deve criar uma categoria com sucesso")
    void shouldCreateCategorySuccessfully() {
        // Arrange
        var user = User.builder().id(UUID.randomUUID()).build();
        var request = new CategoryRequestDTO("Trabalho", "#FF0000");
        mockSecurityContext(user);

        when(categoryRepository.existsByNameAndUserId("Trabalho", user.getId())).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> {
            Category category = invocation.getArgument(0);
            category.setId(UUID.randomUUID());
            return category;
        });

        // Act
        CategoryResponseDTO result = categoryService.createCategory(request);

        // Assert
        assertNotNull(result);
        assertEquals("Trabalho", result.name());
        assertEquals("#FF0000", result.color());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    @DisplayName("Deve lançar CategoryAlreadyExistsException se o nome já existir para o usuário")
    void shouldThrowExceptionWhenCategoryNameExistsForUser() {
        // Arrange
        var user = User.builder().id(UUID.randomUUID()).build();
        var request = new CategoryRequestDTO("Trabalho", "#FF0000");
        mockSecurityContext(user);

        // Simulamos que já existe uma categoria com este nome para este usuário
        when(categoryRepository.existsByNameAndUserId("Trabalho", user.getId())).thenReturn(true);

        // Act & Assert
        assertThrows(CategoryAlreadyExistsException.class, () -> categoryService.createCategory(request));
        verify(categoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve listar todas as categorias do usuário autenticado")
    void shouldListAllCategoriesForUser() {
        // Arrange
        var user = User.builder().id(UUID.randomUUID()).build();
        var category1 = Category.builder().id(UUID.randomUUID()).name("Pessoal").user(user).build();
        var category2 = Category.builder().id(UUID.randomUUID()).name("Estudos").user(user).build();

        mockSecurityContext(user);
        when(categoryRepository.findByUserId(user.getId())).thenReturn(List.of(category1, category2));

        // Act
        List<CategoryResponseDTO> result = categoryService.findAllCategories();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Pessoal", result.get(0).name());
        assertEquals("Estudos", result.get(1).name());
    }

    @Test
    @DisplayName("Deve deletar uma categoria com sucesso se o usuário for o dono")
    void shouldDeleteCategorySuccessfully() {
        // Arrange
        var user = User.builder().id(UUID.randomUUID()).build();
        var category = Category.builder().id(UUID.randomUUID()).user(user).build();

        mockSecurityContext(user);
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        // Act
        categoryService.deleteCategory(category.getId());

        // Assert
        verify(categoryRepository).delete(category);
    }

    @Test
    @DisplayName("Deve atualizar uma categoria com sucesso")
    void shouldUpdateCategorySuccessfully() {
        // Arrange
        var user = User.builder().id(UUID.randomUUID()).build();
        var categoryId = UUID.randomUUID();
        var existingCategory = Category.builder().id(categoryId).name("Antigo").color("#000000").user(user).build();
        var request = new CategoryRequestDTO("Novo Nome", "#FFFFFF");

        mockSecurityContext(user);
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));

        // Act
        CategoryResponseDTO result = categoryService.updateCategory(categoryId, request);

        // Assert
        assertNotNull(result);
        assertEquals("Novo Nome", result.name());
        assertEquals("#FFFFFF", result.color());
        assertEquals("Novo Nome", existingCategory.getName());
    }

    @Test
    @DisplayName("Deve lançar ForbiddenActionException ao tentar atualizar categoria de outro usuário")
    void shouldThrowForbiddenExceptionWhenUpdatingCategoryFromAnotherUser() {
        // Arrange
        var owner = User.builder().id(UUID.randomUUID()).build();
        var intruder = User.builder().id(UUID.randomUUID()).build();
        var categoryId = UUID.randomUUID();
        var category = Category.builder().id(categoryId).user(owner).build();
        var request = new CategoryRequestDTO("Hackeado", "#000000");

        mockSecurityContext(intruder);
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // Act & Assert
        assertThrows(ForbiddenActionException.class, () -> categoryService.updateCategory(categoryId, request));
    }
}
