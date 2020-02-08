package com.donghun.todo.repository;

import com.donghun.todo.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Integer> {

    Todo findByName(String name);

    Todo findByIdx(Integer valueOf);
}
