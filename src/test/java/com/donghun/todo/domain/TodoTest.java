package com.donghun.todo.domain;

import com.donghun.todo.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest
@AutoConfigureTestDatabase
class TodoTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private TodoRepository todoRepository;

    private User user;

    @BeforeEach
    public void createUser() {
        user = User.builder().username("test_username").password("test_password").age(25).build();
        testEntityManager.persist(user);
    }

    @Test
    @DisplayName("Todo 생성 테스트")
    public void createTodoTest() {
        Todo todo = Todo.builder().name("Test Todo").completed(false).createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now()).user(user).build();
        testEntityManager.persist(todo);

        then(todoRepository.getOne(todo.getIdx())).isEqualTo(todo);
    }

    @Test
    @DisplayName("Todo 생성 및 조회 테스트")
    public void createTodoAndSearchTest() {
        Todo todo1 = Todo.builder().name("Test Todo1").completed(false).createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now()).user(user).build();
        testEntityManager.persist(todo1);

        Todo todo2 = Todo.builder().name("Test Todo2").completed(false).createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now()).user(user).build();
        testEntityManager.persist(todo2);

        Todo todo3 = Todo.builder().name("Test Todo3").completed(false).createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now()).user(user).build();
        testEntityManager.persist(todo3);

        List<Todo> todoList = todoRepository.findAll();

        then(todoList).isNotNull();
        then(todoList.size()).isEqualTo(3);
        then(todoList.get(0)).isEqualTo(todo1);
        then(todoList.get(1)).isEqualTo(todo2);
        then(todoList.get(2)).isEqualTo(todo3);
    }

    @Test
    @DisplayName("Todo 생성 및 삭제 테스트")
    public void createTodoAndDeleteTest() {
        Todo todo1 = Todo.builder().name("Test Todo1").completed(false).createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now()).user(user).build();
        testEntityManager.persist(todo1);

        Todo todo2 = Todo.builder().name("Test Todo2").completed(false).createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now()).user(user).build();
        testEntityManager.persist(todo2);

        todoRepository.deleteAll();
        then(todoRepository.findAll().isEmpty()).isTrue();
    }
}