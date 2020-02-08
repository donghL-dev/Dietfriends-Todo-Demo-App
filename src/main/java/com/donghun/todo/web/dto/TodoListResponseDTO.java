package com.donghun.todo.web.dto;

import com.donghun.todo.domain.Todo;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TodoListResponseDTO {

    private Integer idx;

    private String name;

    private Boolean completed;

    private LocalDateTime completedAt;

    private String url;

    public TodoListResponseDTO(Todo todo, String url) {
        this.idx = todo.getIdx();
        this.name = todo.getName();
        this.completed = todo.getCompleted();
        this.completedAt = todo.getCompletedAt();
        this.url = url + "/" + todo.getIdx();
    }
}
