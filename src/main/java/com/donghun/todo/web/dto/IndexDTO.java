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

    private Map<String, Object> endPoints;

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

        Map<String, String> todo = new HashMap<>();
        todo.put("GET", "/todos");
        todo.put("GET(2)", "/todos/{id}");
        todo.put("POST", "/todos");
        todo.put("PUT", "/todos/{id}");
        todo.put("DELETE", "/todos/{id}");

        Map<String, String> user = new HashMap<>();
        user.put("POST", "/user");
        user.put("POST(2)", "/user/auth");
        user.put("DELETE", "/user/logout");

        endPoints.put("todo", todo);
        endPoints.put("user", user);

    }
}
