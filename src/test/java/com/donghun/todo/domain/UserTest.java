package com.donghun.todo.domain;


import com.donghun.todo.repository.UserRepository;
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
class UserTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("유저 생성 테스트")
    public void userCreateTest() {
        User user = User.builder().username("test_username").email("test_email").password("test_password")
                .age(25).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        testEntityManager.persist(user);
        then(userRepository.getOne(user.getIdx())).isNotNull().isEqualTo(user);
    }

    @Test
    @DisplayName("유저 생성 및 조회 테스트")
    public void userCreateAndSearchTest() {
        User user1 = User.builder().username("test_username1").email("test_email1").password("test_password1")
                .age(25).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
        testEntityManager.persist(user1);

        User user2 = User.builder().username("test_username2").email("test_email2").password("test_password2")
                .age(25).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
        testEntityManager.persist(user2);

        User user3 = User.builder().username("test_username3").email("test_email3").password("test_password3")
                .age(25).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
        testEntityManager.persist(user3);

        List<User> users = userRepository.findAll();

        then(users).isNotNull();
        then(users.size()).isEqualTo(3);
        then(users.get(0)).isEqualTo(user1);
        then(users.get(1)).isEqualTo(user2);
        then(users.get(2)).isEqualTo(user3);
    }

    @Test
    @DisplayName("유저 생성 및 삭제 테스트")
    public void userCreateAndDeleteTest() {
        User user1 = User.builder().username("test_username1").email("test_email1").password("test_password1")
                .age(25).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
        testEntityManager.persist(user1);

        User user2 = User.builder().username("test_username2").email("test_email2").password("test_password2")
                .age(25).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
        testEntityManager.persist(user2);

        userRepository.deleteAll();
        then(userRepository.findAll().isEmpty()).isTrue();
    }
}