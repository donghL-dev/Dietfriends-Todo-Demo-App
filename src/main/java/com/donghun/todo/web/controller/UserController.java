package com.donghun.todo.web.controller;

import com.donghun.todo.service.UserService;
import com.donghun.todo.web.dto.LoginDTO;
import com.donghun.todo.web.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult result) {
        logger.info("Create User API Accessed");

        if (result.hasErrors())
            return userService.validation(result);
        else if (userService.isNameCheck(userDTO.getName()))
            return userService.duplicateUser();

        return userService.createUser(userDTO);
    }

    @PostMapping(value = "/auth")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginDTO loginDTO, BindingResult result) {
        logger.info("Login API Accessed");

        if (result.hasErrors())
            return userService.validation(result);
        else if (userService.userCheck(loginDTO))
            return userService.invalidUser();

        return userService.generateToken(loginDTO);
    }

    @DeleteMapping(value = "/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request) {
        logger.info("Logout API Accessed");
        return userService.logoutLogic(request);
    }
}
