package org.todo.todo.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.todo.todo.entity.Todo;
import org.todo.todo.repository.TodoRepository;

import java.util.List;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> index() {
        return todoRepository.findAll();
    }

    public Todo create(Todo todo) {
        if (todo.getText() == null || todo.getText().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Todo text cannot be empty");
        }
        todo.setId(null);
        return todoRepository.save(todo);
    }

    public Todo update(String id, Todo todo) {
        if (todo.getText() != null && todo.getText().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Todo text cannot be empty");
        }
        Todo found = todoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found"));

        if (todo.getText() != null) {
            found.setText(todo.getText());
        }
        found.setDone(todo.isDone());

        return todoRepository.save(found);
    }

    public Todo getById(String id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found"));
    }
}
