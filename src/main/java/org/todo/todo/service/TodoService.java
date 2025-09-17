package org.todo.todo.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.todo.todo.dto.TodoRequest;
import org.todo.todo.dto.TodoResponse;
import org.todo.todo.dto.mapper.TodoMapper;
import org.todo.todo.entity.Todo;
import org.todo.todo.repository.TodoRepository;

import java.util.List;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Tool(name = "FindAllTodo", description = "Find all todos")
    public List<TodoResponse> index() {
        return TodoMapper.toResponseList(todoRepository.findAll());
    }

    @Tool(name = "CreateTodo", description = "Create a new todo")
    public TodoResponse create(TodoRequest todoRequest) {
        Todo todo = TodoMapper.toEntity(todoRequest);
        if (todo.getText() == null || todo.getText().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Todo text cannot be empty");
        }
        todo.setId(null);
        return TodoMapper.toResponse(todoRepository.save(todo));
    }

    @Tool(name = "UpdateTodo", description = "Update an existing todo by id")
    public TodoResponse update(String id, TodoRequest todoRequest) {
        Todo todo = TodoMapper.toEntity(todoRequest);
        if (todo.getText() != null && todo.getText().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Todo text cannot be empty");
        }
        Todo found = todoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found"));

        if (todo.getText() != null) {
            found.setText(todo.getText());
        }
        found.setDone(todo.isDone());

        return TodoMapper.toResponse(todoRepository.save(found));
    }

    @Tool(name = "GetTodoById", description = "Get a todo by id")
    public TodoResponse getById(String id) {
        return TodoMapper.toResponse(todoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found")));
    }

    @Tool(name = "DeleteTodo", description = "Delete a todo by id")
    public void delete(String id) {
        Todo found = todoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found"));
        todoRepository.delete(found);
    }
}
