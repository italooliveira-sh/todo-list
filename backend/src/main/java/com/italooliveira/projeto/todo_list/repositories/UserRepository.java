package com.italooliveira.projeto.todo_list.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.italooliveira.projeto.todo_list.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    boolean existsByName(String name);
    Optional<User> findByEmail(String email);
    
}
