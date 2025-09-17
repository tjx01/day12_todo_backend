package org.todo.todo.config;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.todo.todo.service.TodoService;

@Configuration
public class MCPConfiguration {

    @Bean
    public ToolCallbackProvider toolCallbackProvider(TodoService todoService) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(todoService)
                .build();
    }
}
