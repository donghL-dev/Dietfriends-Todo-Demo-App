package com.donghun.todo.web.controller.common;

import com.donghun.todo.domain.Token;
import com.donghun.todo.domain.User;
import com.donghun.todo.repository.TokenRepository;
import com.donghun.todo.web.auth.SecurityConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class BaseControllerTest {

    @Autowired
    protected WebApplicationContext context;

    @Autowired
    protected TokenRepository tokenRepository;

    protected MockMvc mockMvc;

    @Value("${todo.jjwt.secret}")
    private String secret;

    @Autowired
    protected ObjectMapper objectMapper;

    public String generateToken(User user) {

        byte[] signingKey = secret.getBytes();

        String token = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, signingKey)
                .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
                .setIssuer(SecurityConstants.TOKEN_ISSUER)
                .setAudience(SecurityConstants.TOKEN_AUDIENCE)
                .setSubject("Todo JWT Token")
                .claim("idx", user.getIdx())
                .claim("userName", user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000000000000L))
                .compact();

        tokenRepository.save(Token.builder().token(token).username(user.getUsername()).build());

        return token;
    }
}
