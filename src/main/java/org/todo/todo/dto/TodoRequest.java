package org.todo.todo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodoRequest {
    private String id;
    private String text;
    private boolean done;

    public TodoRequest() {
    }

    public TodoRequest(String id, String text, boolean done) {
        this.id = id;
        this.text = text;
        this.done = done;
    }
}
