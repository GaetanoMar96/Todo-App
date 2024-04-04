package ch.cern.todo.controller;

import ch.cern.todo.BaseIntegrationTest;
import ch.cern.todo.model.Task;
import ch.cern.todo.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(scripts = {"classpath:/scripts/create-h2.sql", "classpath:/scripts/insert-h2.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:/scripts/drop-h2.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class TaskControllerTest extends BaseIntegrationTest {

    @Autowired
    private TaskRepository taskRepository;
    //TODO add JSON pATH matcher
    @Test
    void getAllTasksTest_StatusOk() throws Exception {
        mockMvc.perform(get("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

    }

    @Test
    void getTaskByIdTest_StatusOk() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

    }

    @Test
    void getTaskByIdTest_StatusNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/{id}", 3)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void createTask_StatusCreated() throws Exception {
        Task task = new Task();
        task.setName("frontend development");
        task.setDescription("fe development with angular");
        task.setDeadline(LocalDateTime.now());
        task.setCategoryId(4L);

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(task))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Optional<Task> taskOptional = taskRepository.findById(1L);
        assertTrue(taskOptional.isPresent());
        assertEquals(1L, taskOptional.get().getTaskId());
    }

    @Test
    void deleteTaskByIdTest_StatusOk() throws Exception {
        mockMvc.perform(delete("/api/v1/tasks/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCategoryByIdTest_StatusNotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/tasks/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateTaskByIdTest_StatusOk() throws Exception {
        Optional<Task> taskOptionalPreUpdate = taskRepository.findById(2L);
        LocalDateTime previousDeadLine = getPreviousDeadLine(taskOptionalPreUpdate);
        LocalDateTime currentDeadLine = LocalDateTime.now().plusDays(1);

        mockMvc.perform(patch("/api/v1/tasks/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(currentDeadLine))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Optional<Task> taskOptionalPostUpdate = taskRepository.findById(2L);
        assertTrue(taskOptionalPostUpdate.isPresent());
        assertNotEquals(previousDeadLine, taskOptionalPostUpdate.get().getDeadline());
        //TODO capire perché non è uguale, da riflettere su TIMESTAMP
        //assertEquals(currentDeadLine, taskOptionalPostUpdate.get().getDeadline());

    }

    @Test
    void updateTaskByIdTest_StatusBadRequest_DateInThePast() throws Exception {
        Optional<Task> taskOptionalPreUpdate = taskRepository.findById(2L);
        LocalDateTime previousDeadLine = getPreviousDeadLine(taskOptionalPreUpdate);
        LocalDateTime currentDeadLine = LocalDateTime.of(2024, Month.MARCH, 3, 15, 4);

        mockMvc.perform(patch("/api/v1/tasks/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(currentDeadLine))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        Optional<Task> taskOptionalPostUpdate = taskRepository.findById(2L);
        assertTrue(taskOptionalPostUpdate.isPresent());
        assertEquals(previousDeadLine, taskOptionalPostUpdate.get().getDeadline());
    }

    @Test
    void updateTaskByIdTest_StatusBadRequest_DeadlineNull() throws Exception {
        Optional<Task> taskOptionalPreUpdate = taskRepository.findById(2L);
        LocalDateTime previousDeadLine = getPreviousDeadLine(taskOptionalPreUpdate);
        mockMvc.perform(patch("/api/v1/tasks/{id}", 2)
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
