package org.todo.todo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.todo.todo.entity.Todo;

public interface TodoRepository extends JpaRepository<Todo, String> {
}
