package com.donghun.todo.web.dto;

import lombok.Getter;
import java.util.*;

@Getter
public class IndexDTO {

    private String name;

    private String documentation;

    private String github_repo;

    private Map<String, String> todoModel;

    private Map<String, String> userModel;

    private Map<String, Object> endPoints;

    public IndexDTO() {
        this.name = "Dietfriends Todo Demo App";
        this.documentation = "https://df-test.docs.stoplight.io/api-reference/intro";
        this.github_repo = "https://github.com/donghL-dev/Dietfriends-Todo-Demo-App";

        todoModel = new HashMap<>();
        todoModel.put("name", "String");
        todoModel.put("completed", "Boolean");
        todoModel.put("completedA", "Time");
        todoModel.put("createdAt", "Time");
        todoModel.put("updatedA", "Time");

        userModel = new HashMap<>();
        userModel.put("username", "String");
        userModel.put("password", "String");
        userModel.put("age", "Integer");
        userModel.put("image", "String");
        userModel.put("token", "String");

        endPoints = new HashMap<>();

        Map<String, Object> todo = new HashMap<>();
        List<String> list = Arrays.asList("/todos", "/todos/{todoId}");
        todo.put("GET", list);

        todo.put("POST", "/todos");

        todo.put("PUT", "/todos/{todoId}");

        todo.put("DELETE", "/todos/{todoId}");

        Map<String, Object> user = new HashMap<>();
        list = Arrays.asList("/user", "/user/image/{filename}");
        user.put("GET", list);

        list = Arrays.asList("/user", "/user/auth");
        user.put("POST", list);

        user.put("PUT", "/user/image");

        list = Arrays.asList("/user/logout", "/user/image");
        user.put("DELETE", list);

        endPoints.put("todo", todo);
        endPoints.put("user", user);

    }
}
