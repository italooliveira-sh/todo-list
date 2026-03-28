package com.italooliveira.projeto.todo_list.mappers;

import com.italooliveira.projeto.todo_list.domain.Category;
import com.italooliveira.projeto.todo_list.domain.User;
import com.italooliveira.projeto.todo_list.dto.CategoryRequestDTO;
import com.italooliveira.projeto.todo_list.dto.CategoryResponseDTO;
import com.italooliveira.projeto.todo_list.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryMapper {

    private final TaskRepository taskRepository;

    public Category toEntity(CategoryRequestDTO dto, User user) {
        return Category.builder()
                .name(dto.name())
                .color(dto.color())
                .user(user)
                .build();
    }

    public CategoryResponseDTO toResponseDTO(Category category) {
        long taskCount = taskRepository.countByCategoryId(category.getId());

        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getColor(),
                taskCount
        );
    }
}
