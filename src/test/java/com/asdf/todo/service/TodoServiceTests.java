package com.asdf.todo.service;

import com.asdf.todo.model.Todo;
import com.asdf.todo.repository.TodoInMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TodoServiceTests {

    @Autowired private TodoService todoService;

    @BeforeEach
    void setUp() {
        todoService = new TodoService(new TodoInMemoryRepository());
        todoService.save(new Todo(null, "Test Todo 1", "Description 1", false));
        todoService.save(new Todo(null, "Test Todo 2", "Description 2", true));
    }

    @Test
    void testFindAll() {
        List<Todo> todos = todoService.findAll();
        assertThat(todos).hasSize(2);
    }

    @Test
    void testSaveTodo() {
        Todo todo = new Todo(null, "New Todo", "New Description", false);
        todoService.save(todo);
        assertThat(todoService.findAll()).hasSize(3);
    }

    @Test
    void testFindById() {
        Todo todo = todoService.findById(1L);
        assertThat(todo).isNotNull();
        assertThat(todo.getTitle()).isEqualTo("Test Todo 1");
    }

    @Test
    void testUpdateTodo() {
        Todo updatedTodo =
                new Todo(1L, "Updated Todo", "Updated Description", true);
        todoService.update(1L, updatedTodo);
        Todo todo = todoService.findById(1L);
        assertThat(todo.getTitle()).isEqualTo("Updated Todo");
        assertThat(todo.getDescription()).isEqualTo("Updated Description");
        assertThat(todo.isCompleted()).isTrue();
    }

    @Test
    void testDeleteTodo() {
        todoService.delete(1L);
        assertThat(todoService.findAll()).hasSize(1);
        assertThat(todoService.findById(1L)).isNull();
    }
}