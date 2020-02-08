package com.donghun.todo.web.controller;

import com.donghun.todo.domain.Todo;
import com.donghun.todo.domain.User;
import com.donghun.todo.repository.TodoRepository;
import com.donghun.todo.repository.UserRepository;
import com.donghun.todo.web.auth.SecurityConstants;
import com.donghun.todo.web.controller.common.BaseControllerTest;
import com.donghun.todo.web.dto.TodoDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TodoControllerTest extends BaseControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TodoRepository todoRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();

        user = User.builder().age(25).username("test_user2")
                .password(passwordEncoder.encode("test_password1")).build();
        userRepository.save(user);
    }

    @AfterEach
    public void dbClear() {
        userRepository.deleteAll();
        todoRepository.deleteAll();
        tokenRepository.deleteAll();
    }

    @Test
    @DisplayName("Todo 생성 API 테스트")
    public void createTodoTest() throws Exception {
        String token = generateToken(user);

        TodoDTO todoDTO = new TodoDTO();
        todoDTO.setName("Test Todo Create");

        mockMvc.perform(post("/todos")
                .header(SecurityConstants.TOKEN_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(todoDTO)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(status().isCreated());

        Todo todo = todoRepository.findByName(todoDTO.getName());

        then(todo).isNotNull();
        then(todo.getName()).isEqualTo(todoDTO.getName());
    }

    @Test
    @DisplayName("단일 Todo 조회 API 테스트")
    public void getTodoTest() throws Exception {
        Todo todo = todoRepository.save(Todo.builder().name("조회를 위한 Todo").user(user)
                .updatedAt(LocalDateTime.now()).createdAt(LocalDateTime.now())
                .build());

        mockMvc.perform(get("/todos/" + todo.getIdx()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("List Todo 조회 API 테스트")
    public void getListTodoTest() throws Exception {
        todoRepository.save(Todo.builder().name("조회를 위한 Todo").user(user)
                .updatedAt(LocalDateTime.now()).createdAt(LocalDateTime.now())
                .build());

        mockMvc.perform(get("/todos"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Todo 수정 API 테스트")
    public void putTodoTest() throws Exception {
        Todo originalTodo = todoRepository.save(Todo.builder().name("수정을 위한 Todo").user(user)
                .updatedAt(LocalDateTime.now()).createdAt(LocalDateTime.now())
                .build());

        String token = generateToken(user);

        TodoDTO todoDTO = new TodoDTO();
        todoDTO.setName("수정된 Todo");

        mockMvc.perform(put("/todos/" + originalTodo.getIdx())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(todoDTO))
                .header(SecurityConstants.TOKEN_HEADER, token))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(status().isOk());

        Todo modifyTodo = todoRepository.findByName("수정을 위한 Todo");
        then(modifyTodo).isNull();

        modifyTodo = todoRepository.findByName("수정된 Todo");
        then(modifyTodo).isNotNull().isEqualTo(originalTodo);
    }

    @Test
    @DisplayName("Todo 삭제 API 테스트")
    public void deleteTodoTest() throws Exception {
        Todo todo = todoRepository.save(Todo.builder().name("삭제를 위한 Todo").user(user)
                .updatedAt(LocalDateTime.now()).createdAt(LocalDateTime.now())
                .build());

        String token = generateToken(user);

        mockMvc.perform(delete("/todos/" + todo.getIdx())
                .header(SecurityConstants.TOKEN_HEADER, token))
                .andExpect(status().isNoContent());

        Todo newTodo = todoRepository.findByName("삭제를 위한 Todo");
        then(newTodo).isNull();

        List<Todo> todoList = todoRepository.findAll();
        then(todoList.isEmpty()).isTrue();
    }




}