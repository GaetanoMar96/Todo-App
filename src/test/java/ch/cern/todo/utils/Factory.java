package ch.cern.todo.utils;

import ch.cern.todo.model.Category;
import ch.cern.todo.model.Task;

import java.time.LocalDateTime;

public class Factory {

    public static Task getTask(String name, String description, LocalDateTime deadline, Long categoryId) {
        return Task.builder()
                .name(name)
                .description(description)
                .deadline(deadline)
                .categoryId(categoryId)
                .build();
    }

    public static Category getCategory(String name, String description) {
        return Category.builder()
                .name(name)
                .description(description)
                .build();
    }
}
