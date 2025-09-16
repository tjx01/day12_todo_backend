package org.todo.todo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.todo.todo.dto.TodoRequest;
import org.todo.todo.dto.TodoResponse;
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
    List<TodoResponse> index() {
        return todoService.index();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    TodoResponse create(@RequestBody TodoRequest todoRequest) {
        return todoService.create(todoRequest);
    }

    @GetMapping("/{id}")
    TodoResponse getById(@PathVariable String id) {
        return todoService.getById(id);
    }

    @PutMapping("/{id}")
    TodoResponse update(@PathVariable String id, @RequestBody TodoRequest todoRequest) {
        return todoService.update(id, todoRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable String id) {
        todoService.delete(id);
    }
}
