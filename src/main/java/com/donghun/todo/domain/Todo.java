package com.donghun.todo.domain;

import com.donghun.todo.web.dto.TodoDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter @Setter @ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "idx")
@Entity @Table @Builder
public class Todo implements Serializable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @Column(nullable = false)
    private String name;

    @Column()
    private Boolean completed;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "Asia/Seoul")
    private LocalDateTime completedAt;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private User user;

    public Todo updateTodo(TodoDTO todoDTO) {
        this.name = todoDTO.getName();
        this.completed = todoDTO.getCompleted();
        this.completedAt = todoDTO.getCompleted() != null
                && todoDTO.getCompleted() ? LocalDateTime.now() : null;
        this.updatedAt = LocalDateTime.now();

        return this;
    }
}
