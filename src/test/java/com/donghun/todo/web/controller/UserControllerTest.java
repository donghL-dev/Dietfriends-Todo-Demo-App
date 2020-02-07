package com.donghun.todo.web.controller;

import com.donghun.todo.domain.Token;
import com.donghun.todo.domain.User;
import com.donghun.todo.repository.TokenRepository;
import com.donghun.todo.repository.UserRepository;
import com.donghun.todo.web.dto.LoginDTO;
import com.donghun.todo.web.dto.UserDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest extends BaseControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @AfterEach
    public void init() {
        tokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("User 생성 테스트")
    public void createUserTest() throws Exception {
        UserDTO userDTO = UserDTO.builder().age(25).name("Test_UserName")
                .password("Test@Password1234").build();

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated());

        User user = userRepository.findByUsername("Test_UserName");

        then(user).isNotNull();
        then(user.getUsername()).isEqualTo("Test_UserName");
        then(user.getAge()).isEqualTo(25);
    }

    @Test
    @DisplayName("User 로그인 API 테스트")
    public void loginTest() throws Exception {

        User user = User.builder().age(25).username("test_user2")
                .password(passwordEncoder.encode("test_password1")).build();
        userRepository.save(user);

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setName("test_user2");
        loginDTO.setPassword("test_password1");

        mockMvc.perform(post("/user/auth")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                        .andExpect(status().isCreated());

        User saveduser = userRepository.findByUsername("test_use2r");
        Token token = tokenRepository.findByUsername("test_use2r");

        then(token).isNotNull();
        then(token.getToken()).isEqualTo(saveduser.getToken());
    }

    @Test
    @DisplayName("User 로그아웃 API 테스트")
    public void logoutTest() throws Exception {

        User user = User.builder().age(25).username("test_user3")
                .password(passwordEncoder.encode("test_password1")).build();
        userRepository.save(user);

        String token = generateToken(user);
        tokenRepository.save(Token.builder().username(user.getUsername()).token(token).build());

        mockMvc.perform(delete("/user/logout")
                .header("Authorization", token))
                .andExpect(status().isOk());

        then(tokenRepository.findAll().isEmpty()).isTrue();
    }

}