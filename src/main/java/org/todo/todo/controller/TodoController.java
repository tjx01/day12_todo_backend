package org.todo.todo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.todo.todo.entity.Todo;
import org.todo.todo.service.TodoService;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    List<Todo> index() {
        return todoService.index();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Todo create(@RequestBody Todo todo) {
        return todoService.create(todo);
    }

    @GetMapping("/{id}")
    Todo getById(@PathVariable String id) {
        return todoService.getById(id);
    }

    @PutMapping("/{id}")
    Todo update(@PathVariable String id, @RequestBody Todo todo) {
        return todoService.update(id, todo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable String id) {
        todoService.delete(id);
    }
}
