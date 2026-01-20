package com.asdf.todo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Todo {
    private Long id;
    @NonNull private String title;
    private String description;
    private boolean completed;
}
