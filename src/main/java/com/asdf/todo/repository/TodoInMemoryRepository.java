package com.asdf.todo.repository;

import com.asdf.todo.model.Todo;
import org.springframework.stereotype.Repository;

import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TodoInMemoryRepository {
    private final Map<Long, Todo> todoMapodoMap = new HashMap();
    private final AtomicLong counter = new AtomicLong();

    public List<Todo> findAll() {
        return new ArrayList<>(todoMap.values());

    }

    public Todo findbyId(Long id) {
        return todoMap.get(id);
    }

    public Todo save(Todo todo) {
        if(todo.getId() == null) {
            todo.setId(counter.incrementAndGet());

        }
        todoMap.put(todo.getId(), todo);
        return todo;
    }

    public void deleteById(Long id) {
        todoMap.remove(id);
    }

}
