package com.asdf.todo.repository;

import com.asdf.todo.controller.TodoController;
import com.asdf.todo.model.Todo;
import com.asdf.todo.service.TodoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TodoController.class)
public class TodoControllerTests {

    @Autowired private MockMvc mockMvc;
    @MockBean private TodoService todoService;

    @Test
    public void testGetTodoById() throws Exception {
        Todo todo = new Todo();
        todo.setId(1L);
        todo.setTitle("Test Todo");

        given(todoService.findById(1L)).willReturn(todo);

        mockMvc.perform(get("/api/todos/v1/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1)) // JSON에서는 숫자타입으로 비교하므로 1L 대신 1도 가능
                .andExpect(jsonPath("$.title").value("Test Todo"));
    }

    @Test
    public void testGetAllTodos() throws Exception {
        // [수정] collections -> Collections (대문자)
        given(todoService.findAll()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/api/todos/v1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // [참고] Todo 클래스에 해당 생성자가 있는지 확인 필요
        given(todoService.findAll())
                .willReturn(
                        Collections.singletonList(
                                new Todo(1L, "Test Todo", "Description", false)));

        mockMvc.perform(get("/api/todos/v1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Todo"));
    }

    @Test
    public void testCreateTodo() throws Exception {
        Todo todo = new Todo();
        todo.setId(1L);
        todo.setTitle("New Todo");

        given(todoService.save(any(Todo.class))).willReturn(todo);

        mockMvc.perform(
                        post("/api/todos/v1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"title\":\"New Todo\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("New Todo"));
    }

    @Test
    public void testUpdateTodo() throws Exception {
        Todo existingTodo = new Todo();
        existingTodo.setId(1L);
        existingTodo.setTitle("Existing Todo");

        Todo updatedTodo = new Todo();
        updatedTodo.setId(1L); // [수정] 1l -> 1L (대문자)
        updatedTodo.setTitle("Updated Todo");

        given(todoService.findById(1L)).willReturn(existingTodo);
        given(todoService.update(anyLong(), any(Todo.class)))
                .willReturn(updatedTodo);

        mockMvc.perform(
                        put("/api/todos/v1/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"title\":\"Updated Todo\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated Todo"));
    }

    @Test
    public void testDeleteTodo() throws Exception {
        Todo todo = new Todo();
        todo.setId(1L);
        todo.setTitle("Test Todo");

        given(todoService.findById(1L)).willReturn(todo);

        mockMvc.perform(delete("/api/todos/v1/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}