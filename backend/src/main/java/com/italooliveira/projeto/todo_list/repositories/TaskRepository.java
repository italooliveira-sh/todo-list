package com.italooliveira.projeto.todo_list.repositories;

import com.italooliveira.projeto.todo_list.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByUserId(UUID userId);
    long countByCategoryId(UUID categoryId);
}