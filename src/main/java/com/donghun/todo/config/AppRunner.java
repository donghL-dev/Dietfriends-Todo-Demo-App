package com.donghun.todo.config;

import com.donghun.todo.domain.Todo;
import com.donghun.todo.domain.User;
import com.donghun.todo.repository.TodoRepository;
import com.donghun.todo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class AppRunner implements CommandLineRunner {

    private final UserRepository userRepository;

    private final TodoRepository todoRepository;

    private Random random = new Random();

    @Override
    public void run(String... args) throws Exception {
        IntStream.rangeClosed(1, 10).forEach(index -> userRepository.save(createUser(index)));
        IntStream.rangeClosed(1, 50).forEach(index -> todoRepository.save(createTodo(index)));
    }

    private User createUser(int index) {
        int age = random.nextInt(150) + 1;
        return User.builder().username("Todo User " + index).password("TodoPassword@!" + index)
                .age(age).image("https://donghun.dev:8085/user/image/USER_DEFAULT_PROFILE_IMG.png")
                .build();
    }

    private Todo createTodo(int index) {
        int userIdx = random.nextInt(10) + 1;
        return Todo.builder().name("Hello World Todo " + index).createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now()).user(userRepository.findByIdx(userIdx)).build();
    }
}
