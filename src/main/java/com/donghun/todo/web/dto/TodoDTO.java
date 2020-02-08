package com.donghun.todo.web.dto;

import com.donghun.todo.domain.Todo;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter @Setter
public class TodoDTO {

    @NotBlank(message = "name is null")
    private String name;

    private Boolean completed;

    public Todo createTodo() {
        return Todo.builder().name(name).completed(completed).createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now()).completedAt(completed != null
                        && completed ? LocalDateTime.now() : null).build();
    }
}
