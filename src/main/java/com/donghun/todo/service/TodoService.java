package com.donghun.todo.service;

import com.donghun.todo.domain.Todo;
import com.donghun.todo.domain.User;
import com.donghun.todo.repository.TodoRepository;
import com.donghun.todo.repository.TokenRepository;
import com.donghun.todo.repository.UserRepository;
import com.donghun.todo.service.commons.BaseService;
import com.donghun.todo.web.auth.JwtResolver;
import com.donghun.todo.web.dto.ErrorResponseDTO;
import com.donghun.todo.web.dto.TodoDTO;
import com.donghun.todo.web.dto.TodoListResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoService extends BaseService {

    private final UserRepository userRepository;

    private final TodoRepository todoRepository;

    private final TokenRepository tokenRepository;

    private final JwtResolver jwtResolver;

    private ErrorResponseDTO response;

    public Integer tokenCheck(HttpServletRequest request) {
        if (jwtResolver.isTokenExist(request)) {
            return 1;
        } else if (jwtResolver.validateToken(request)
                || tokenRepository.findByToken(jwtResolver.getToken(request)) == null) {
            return 2;
        }

        return 3;
    }

    public Integer isAuthorCheck(HttpServletRequest request, String tokenId) {
        User currentUser = userRepository.findByIdx(jwtResolver.getUserByToken(request));
        Todo currentTodo = todoRepository.findByIdx(Integer.valueOf(tokenId));

        if (currentUser == null || currentTodo == null)
            return 4;
        else if (!currentUser.equals(currentTodo.getUser()))
            return 5;

        return 6;
    }

    public ResponseEntity<?> errorResponse(Integer flag) {
        if (flag == 1 || flag == 5) {
            response = ErrorResponseDTO.builder().status("401").error("Not Authorized")
                    .message("권한이 없습니다.").build();

            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        } else if (flag == 4) {
            response = ErrorResponseDTO.builder().status("500").error("Server Error")
                    .message("유저 또는 Todo가 존재하지 않습니다.").build();

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response = ErrorResponseDTO.builder().status("401").error("Not Authorized")
                .message("유효하지 않은 토큰입니다.").build();

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    public boolean isStorageCheck() {
        return todoRepository.findAll().size() >= 1000000;
    }

    public ResponseEntity<?> createTodo(TodoDTO todoDTO, HttpServletRequest request) {
        User user = userRepository.findByIdx(jwtResolver.getUserByToken(request));
        Todo todo = todoDTO.createTodo();
        user.addTodo(todo);
        return new ResponseEntity<>(todoRepository.save(todo), HttpStatus.CREATED);
    }

    public ResponseEntity<?> getTodo(String todoId) {
        Optional<Todo> todo = todoRepository.findById(Integer.valueOf(todoId));
        response = ErrorResponseDTO.builder().status("404").error("Not Found")
                .message("todo is not exist.").build();

        return todo.isPresent() ? new ResponseEntity<>(todo.get(), HttpStatus.OK) :
                new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> getListTodo(Integer limit, String skip) {
        List<TodoListResponseDTO> todos = todoRepository.findAll().stream().sorted((a, b) -> (b.getIdx())
                .compareTo(a.getIdx())).skip(skip == null ? 0 : Integer.valueOf(skip))
                .limit(Optional.ofNullable(limit)
                .map(i -> i > 100 ? 100 : limit).orElse(100))
                .map(todo -> new TodoListResponseDTO(todo, getCurrentUri()))
                .collect(Collectors.toList());

        response = ErrorResponseDTO.builder().status("404").error("Not Found")
                .message("todos is not exist.").build();

        return todos != null ? new ResponseEntity<>(todos, HttpStatus.OK) :
                new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    public String getCurrentUri() {
        return ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
    }

    public ResponseEntity<?> putTodo(String todoId, TodoDTO todoDTO) {
        Todo todo = todoRepository.findByIdx(Integer.valueOf(todoId));
        todoRepository.save(todo.updateTodo(todoDTO));

        return new ResponseEntity<>(todo, HttpStatus.OK);
    }

    public ResponseEntity<?> deleteTodo(String todoId) {
        Todo todo = todoRepository.findByIdx(Integer.valueOf(todoId));
        todoRepository.delete(todo);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
