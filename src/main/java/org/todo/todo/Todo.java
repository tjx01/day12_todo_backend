package org.todo.todo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class Todo {
    @Id
    private String id;
    private String text;
    private boolean done;


    public Todo() {

    }

    public Todo(String id, String text, boolean done) {
        this.text = text;
        this.done = done;
        this.id = id;
    }

    @PrePersist
    public void ensureId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
