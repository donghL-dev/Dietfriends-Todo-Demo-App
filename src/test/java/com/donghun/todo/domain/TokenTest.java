package com.donghun.todo.domain;

import com.donghun.todo.repository.TokenRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest
@AutoConfigureTestDatabase
class TokenTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private TokenRepository tokenRepository;

    @Test
    @DisplayName("Token 생성 테스트")
    public void createTokenTest() {
        Token token = Token.builder().token(UUID.randomUUID().toString()).username("test_user").build();
        testEntityManager.persist(token);
        then(tokenRepository.getOne(token.getIdx())).isNotNull().isEqualTo(token);
    }

    @Test
    @DisplayName("Token 생성 및 조회 테스트")
    public void createTokenAndSearchTest() {
        Token token1 = Token.builder().token(UUID.randomUUID().toString()).username("test_user1").build();
        testEntityManager.persist(token1);

        Token token2 = Token.builder().token(UUID.randomUUID().toString()).username("test_user2").build();
        testEntityManager.persist(token2);

        Token token3 = Token.builder().token(UUID.randomUUID().toString()).username("test_user3").build();
        testEntityManager.persist(token3);

        List<Token> tokenList = tokenRepository.findAll();

        then(tokenList).isNotNull();
        then(tokenList.size()).isEqualTo(3);
        then(tokenList.get(0)).isEqualTo(token1);
        then(tokenList.get(1)).isEqualTo(token2);
        then(tokenList.get(2)).isEqualTo(token3);
    }

    @Test
    @DisplayName("Token 생성 및 삭제 테스트")
    public void createTokenAndDeleteTest() {
        Token token1 = Token.builder().token(UUID.randomUUID().toString()).username("test_user1").build();
        testEntityManager.persist(token1);

        Token token2 = Token.builder().token(UUID.randomUUID().toString()).username("test_user2").build();
        testEntityManager.persist(token2);

        tokenRepository.deleteAll();
        then(tokenRepository.findAll().isEmpty()).isTrue();
    }

}