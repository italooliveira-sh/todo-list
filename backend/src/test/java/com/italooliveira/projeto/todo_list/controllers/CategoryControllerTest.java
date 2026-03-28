package com.italooliveira.projeto.todo_list.controllers;

import com.italooliveira.projeto.todo_list.dto.CategoryRequestDTO;
import com.italooliveira.projeto.todo_list.dto.CategoryResponseDTO;
import com.italooliveira.projeto.todo_list.services.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CategoryController.class, excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class
})
class CategoryControllerTest extends BaseControllerTest {

    @MockitoBean
    private CategoryService categoryService;

    @Test
    @DisplayName("Deve retornar 201 ao criar uma categoria com sucesso")
    void shouldReturn201WhenCreatingCategorySuccessfully() throws Exception {
        var request = new CategoryRequestDTO("Trabalho", "#FF0000");
        var response = new CategoryResponseDTO(UUID.randomUUID(), "Trabalho", "#FF0000", 0L);

        when(categoryService.createCategory(any(CategoryRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Trabalho"))
                .andExpect(jsonPath("$.color").value("#FF0000"));
    }

    @Test
    @DisplayName("Deve retornar 400 ao tentar criar categoria com nome inválido")
    void shouldReturn400WhenCreatingWithInvalidName() throws Exception {
        var invalidRequest = new CategoryRequestDTO("", "#invalid");

        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Campos inválidos"))
                .andExpect(jsonPath("$.errors.name").value("O nome da categoria é obrigatório"));
    }

    @Test
    @DisplayName("Deve retornar 200 ao listar categorias do usuário")
    void shouldReturn200WhenListingCategories() throws Exception {
        var responseList = List.of(
                new CategoryResponseDTO(UUID.randomUUID(), "Trabalho", "#FF0000", 0L),
                new CategoryResponseDTO(UUID.randomUUID(), "Pessoal", "#00FF00", 0L)
        );

        when(categoryService.findAllCategories()).thenReturn(responseList);

        mockMvc.perform(get("/api/categories")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Trabalho"))
                .andExpect(jsonPath("$[1].name").value("Pessoal"));
    }

    @Test
    @DisplayName("Deve retornar 200 ao atualizar uma categoria com sucesso")
    void shouldReturn200WhenUpdatingCategory() throws Exception {
        var categoryId = UUID.randomUUID();
        var request = new CategoryRequestDTO("Atualizado", "#123456");
        var response = new CategoryResponseDTO(categoryId, "Atualizado", "#123456", 0L);

        when(categoryService.updateCategory(eq(categoryId), any(CategoryRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/categories/{id}", categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Atualizado"))
                .andExpect(jsonPath("$.color").value("#123456"));
    }

    @Test
    @DisplayName("Deve retornar 204 ao deletar uma categoria com sucesso")
    void shouldReturn204WhenDeletingCategory() throws Exception {
        var categoryId = UUID.randomUUID();

        mockMvc.perform(delete("/api/categories/{id}", categoryId))
                .andExpect(status().isNoContent());
    }
}
