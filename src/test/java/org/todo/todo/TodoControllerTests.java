package org.todo.todo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.todo.todo.entity.Todo;
import org.todo.todo.repository.TodoRepository;

import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TodoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoRepository todoRepository;

    @BeforeEach
    void setup() {
        todoRepository.deleteAll();
    }

    @Test
    void should_return_empty_when_no_todos() throws Exception {
        MockHttpServletRequestBuilder request = get("/todos")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void should_response_ont_todo_with_id_1() throws Exception {
        Todo todo = new Todo(null, "Buy milk", false);
        todoRepository.save(todo);

        MockHttpServletRequestBuilder request = get("/todos")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].length()").value(3))
                .andExpect(jsonPath("$[0].text").value("Buy milk"))
                .andExpect(jsonPath("$[0].done").value(false));
    }

    @Test
    void should_response_201_when_create_todo() throws Exception {
        MockHttpServletRequestBuilder request = post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "text": "Buy milk",
                            "done": false
                        }
                        """);


        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.text").value("Buy milk"))
                .andExpect(jsonPath("$.done").value(false));
    }

    @Test
    void should_response_422_when_create_todo_without_text() throws Exception {
        MockHttpServletRequestBuilder request = post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "done": false
                        }
                        """);
        mockMvc.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void should_response_422_when_create_todo_with_text_empty() throws Exception {
        MockHttpServletRequestBuilder request = post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "text": "",
                            "done": false
                        }
                        """);
        mockMvc.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void should_Ignore_client_sent_id_and_generate_a_new_id() throws Exception {
        MockHttpServletRequestBuilder request = post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "id": "client-sent-id",
                            "text": "Buy milk",
                            "done": false
                        }
                        """);
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(not("client-sent-id")));
    }

    @Test
    void should_response_new_todo_when_update_todo() throws Exception {
        Todo todo = todoRepository.save(new Todo(null, "Buy milk", false));

        MockHttpServletRequestBuilder request = put("/todos/" + todo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "text": "Buy bread",
                            "done": true
                        }
                        """);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.text").value("Buy bread"))
                .andExpect(jsonPath("$.done").value(true));
    }

    @Test
    void should_response_new_todo_when_update_todo_with_two_ids() throws Exception {
        Todo todo = todoRepository.save(new Todo(null, "Buy milk", false));
        Todo todo2 = todoRepository.save(new Todo(null, "Buy bread", true));
        MockHttpServletRequestBuilder request = put("/todos/" + todo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format("""
                        {
                          "text": "Buy banana",
                          "done": true,
                          "id": "%s"
                        }
                        """, todo2.getId()));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(todo.getId()))
                .andExpect(jsonPath("$.text").value("Buy banana"))
                .andExpect(jsonPath("$.done").value(true));
    }

    @Test
    void should_response_404_when_update_todo_id_not_exist() throws Exception {
        MockHttpServletRequestBuilder request = put("/todos/123")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "text": "Buy bread",
                            "done": true
                        }
                        """);
        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    void should_response_422_when_update_todo_with_text_empty() throws Exception {
        Todo todo = todoRepository.save(new Todo(null, "Buy milk", false));
        MockHttpServletRequestBuilder request = put("/todos/" + todo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "text": "",
                            "done": true
                        }
                        """);
        mockMvc.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }
}
