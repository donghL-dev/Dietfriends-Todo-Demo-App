package com.donghun.todo.web.controller;

import com.donghun.todo.service.TodoService;
import com.donghun.todo.web.dto.ErrorResponseDTO;
import com.donghun.todo.web.dto.TodoDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/todos")
@RequiredArgsConstructor
public class TodoController {

    private final Logger logger = LoggerFactory.getLogger(TodoController.class);

    private final TodoService todoService;

    @PostMapping
    public ResponseEntity<?> createTodo(@Valid @RequestBody TodoDTO todoDTO, BindingResult result,
                                        HttpServletRequest request) {
        logger.info("Create Todo API Accessed");

        if (todoService.tokenCheck(request) != 3)
            return todoService.errorResponse(todoService.tokenCheck(request));
        else if (result.hasErrors())
            return todoService.validation(result);
        else if (todoService.isStorageCheck()) {
            ErrorResponseDTO response = new ErrorResponseDTO("500", "Server Error",
                    "Todo는 최대 1000000개 까지만 저장할 수 있습니다.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return todoService.createTodo(todoDTO, request);
    }

    @GetMapping(value = "/{todoId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getTodo(@PathVariable String todoId) {
        logger.info("Get Todo API Accessed");
        return todoService.getTodo(todoId);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getListTodo(@RequestParam(required = false) Integer limit,
                                         @RequestParam(required = false) String skip) {
        logger.info("Get List Todo API Accessed");
        return todoService.getListTodo(limit, skip);
    }

    @PutMapping(value = "/{todoId}")
    public ResponseEntity<?> putTodo(@PathVariable String todoId, HttpServletRequest request,
                                     @Valid @RequestBody TodoDTO todoDTO, BindingResult result) {
        logger.info("Put Todo API Accessed");

        if (todoService.tokenCheck(request) != 3)
            return todoService.errorResponse(todoService.tokenCheck(request));
        else if (todoService.isAuthorCheck(request, todoId) != 6)
            return todoService.errorResponse(todoService.isAuthorCheck(request, todoId));
        else if (result.hasErrors())
            return todoService.validation(result);

        return todoService.putTodo(todoId, todoDTO);
    }

    @DeleteMapping(value = "/{todoId}")
    public ResponseEntity<?> deleteTodo(@PathVariable String todoId, HttpServletRequest request) {
        logger.info("Delete Todo API Accessed");

        if (todoService.tokenCheck(request) != 3)
            return todoService.errorResponse(todoService.tokenCheck(request));
        else if (todoService.isAuthorCheck(request, todoId) != 6)
            return todoService.errorResponse(todoService.isAuthorCheck(request, todoId) );

        return todoService.deleteTodo(todoId);
    }
}
