package com.donghun.todo.repository;

import com.donghun.todo.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    Token findByUsername(String test_user);

    @Transactional
    void deleteByToken(String token);

    Token findByToken(String token);
}
