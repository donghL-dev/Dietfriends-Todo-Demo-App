package com.donghun.todo.web.dto;

import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
@Getter
public class IndexDTO {

    private String name;

    private String documentation;

    private Map<String, String> todoModel;

    private Map<String, String> endPoints;

    public IndexDTO() {
        this.name = "Dietfriends Todo Demo App";
        this.documentation = "https://df-test.docs.stoplight.io/api-reference/intro";

        todoModel = new HashMap<>();
        todoModel.put("name", "String");
        todoModel.put("completed", "Boolean");
        todoModel.put("completedA", "Time");
        todoModel.put("createdAt", "Time");
        todoModel.put("updatedA", "Time");

        endPoints = new HashMap<>();
        endPoints.put("GET", "/todos");
        endPoints.put("GET", "/todos/{id}");
        endPoints.put("POST", "/todos");
        endPoints.put("PUT", "/todos/{id}");
        endPoints.put("DELETE", "/todos/{id}");
    }
}
