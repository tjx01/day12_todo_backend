package org.todo.todo.dto.mapper;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.todo.todo.dto.TodoRequest;
import org.todo.todo.dto.TodoResponse;
import org.todo.todo.entity.Todo;

import java.util.List;

@Component
public class TodoMapper {
    public static TodoResponse toResponse(Todo todo) {
        TodoResponse response = new TodoResponse();
        BeanUtils.copyProperties(todo, response);
        return response;
    }

    public static List<TodoResponse> toResponseList(List<Todo> todos) {
        return todos.stream().map(TodoMapper::toResponse).toList();
    }

    public static Todo toEntity(TodoRequest request) {
        Todo todo = new Todo();
        BeanUtils.copyProperties(request, todo);
        return todo;
    }

}
