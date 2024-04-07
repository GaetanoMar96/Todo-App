package ch.cern.todo.controller;

import ch.cern.todo.BaseIntegrationTest;
import ch.cern.todo.model.Task;
import ch.cern.todo.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class TaskControllerTest extends BaseIntegrationTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void getAllTasksTest_StatusOk() throws Exception {
        mockMvc.perform(get("/api/v1/todo/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("backend development"))
                .andExpect(jsonPath("$[0].description").value("be development in java"));
    }

    @Test
    void getTaskByIdTest_StatusOk() throws Exception {
        mockMvc.perform(get("/api/v1/todo/tasks/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("backend development"))
                .andExpect(jsonPath("$.description").value("be development in java"));

    }

    @Test
    void getTaskByIdTest_StatusNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/todo/tasks/{id}", 3)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void createTask_StatusCreated() throws Exception {
        Task task = new Task();
        task.setName("frontend development");
        task.setDescription("fe development with angular");
        task.setDeadline(LocalDateTime.now().plusDays(1));
        task.setCategoryId(1L);

        mockMvc.perform(post("/api/v1/todo/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(task))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Optional<Task> taskOptional = taskRepository.findById(2L);
        assertTrue(taskOptional.isPresent());
        assertEquals(2L, taskOptional.get().getTaskId());
        assertEquals(2, taskRepository.findAll().size());
    }

    @Test
    void deleteTaskByIdTest_StatusOk() throws Exception {
        mockMvc.perform(delete("/api/v1/todo/tasks/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertTrue(taskRepository.findAll().isEmpty());
    }

    @Test
    void deleteCategoryByIdTest_StatusNotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/todo/tasks/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateTaskByIdTest_StatusOk() throws Exception {
        Optional<Task> taskOptionalPreUpdate = taskRepository.findById(1L);
        LocalDateTime previousDeadLine = getPreviousDeadLine(taskOptionalPreUpdate);
        LocalDateTime currentDeadLine = LocalDateTime.now().plusDays(1);

        mockMvc.perform(patch("/api/v1/todo/tasks/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(currentDeadLine))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Optional<Task> taskOptionalPostUpdate = taskRepository.findById(1L);
        assertTrue(taskOptionalPostUpdate.isPresent());
        assertNotEquals(previousDeadLine, taskOptionalPostUpdate.get().getDeadline());
        assertTrue(taskOptionalPostUpdate.get().getDeadline().isAfter(LocalDateTime.now()));
    }

    @Test
    void updateTaskByIdTest_StatusBadRequest_DateInThePast() throws Exception {
        Optional<Task> taskOptionalPreUpdate = taskRepository.findById(1L);
        LocalDateTime previousDeadLine = getPreviousDeadLine(taskOptionalPreUpdate);
        LocalDateTime currentDeadLine = LocalDateTime.of(2024, Month.MARCH, 3, 15, 4);

        mockMvc.perform(patch("/api/v1/todo/tasks/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(currentDeadLine))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        Optional<Task> taskOptionalPostUpdate = taskRepository.findById(1L);
        assertTrue(taskOptionalPostUpdate.isPresent());
        assertEquals(previousDeadLine, taskOptionalPostUpdate.get().getDeadline());
    }

    @Test
    void updateTaskByIdTest_StatusBadRequest_DeadlineNull() throws Exception {
        Optional<Task> taskOptionalPreUpdate = taskRepository.findById(1L);
        LocalDateTime previousDeadLine = getPreviousDeadLine(taskOptionalPreUpdate);
        mockMvc.perform(patch("/api/v1/todo/tasks/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(null))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        taskOptionalPreUpdate.ifPresent(task -> assertEquals(previousDeadLine, task.getDeadline()));
    }

    private LocalDateTime getPreviousDeadLine(Optional<Task> taskOptionalPreUpdate) {
        LocalDateTime previousDeadLine = null;
        if (taskOptionalPreUpdate.isPresent()) {
            previousDeadLine = taskOptionalPreUpdate.get().getDeadline();
        }
        return previousDeadLine;
    }
}
