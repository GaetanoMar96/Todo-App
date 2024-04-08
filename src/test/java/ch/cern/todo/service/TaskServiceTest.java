package ch.cern.todo.service;

import ch.cern.todo.exception.BadRequestException;
import ch.cern.todo.exception.DataNotFoundException;
import ch.cern.todo.model.Category;
import ch.cern.todo.model.Task;
import ch.cern.todo.repository.CategoryRepository;
import ch.cern.todo.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @Test
    void testGetAllTasks() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(1L, "Code refactor", "Java code refactor", LocalDateTime.now(), 1L));
        tasks.add(new Task(2L, "Code refactor", "Python code refactor", LocalDateTime.now(), 2L));

        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = taskService.getAllTasks();

        assertEquals(tasks.size(), result.size());
        assertArrayEquals(tasks.toArray(), result.toArray());
    }

    @Test
    void testGetTaskById() {
        Task task = new Task(1L, "Code refactor", "Java code refactor", LocalDateTime.now(), 1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Optional<Task> result = taskService.getTaskById(1L);

        assertTrue(result.isPresent());
        assertEquals(task, result.get());
    }

    @Test
    void testCreateTaskValidCategoryId() {
        Task task = new Task(1L, "Code refactor", "Java code refactor", LocalDateTime.now().plusDays(1), 1L);
        Category category = new Category(1L, "Refactor", "Code refactor");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        taskService.createTask(task);

        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testCreateTaskInvalidCategoryId() {
        Task task = new Task(1L, "Code refactor", "Java code refactor", LocalDateTime.now(), 99L);

        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> taskService.createTask(task));
        verify(taskRepository, never()).save(task);
    }

    @Test
    void testDeleteTaskByIdExists() {
        Long taskId = 1L;
        when(taskRepository.existsById(taskId)).thenReturn(true);

        assertTrue(taskService.deleteTaskById(taskId));
        verify(taskRepository, times(1)).deleteById(taskId);
    }

    @Test
    void testDeleteTaskByIdDoesNotExist() {
        Long taskId = 1L;
        when(taskRepository.existsById(taskId)).thenReturn(false);

        assertFalse(taskService.deleteTaskById(taskId));
        verify(taskRepository, never()).deleteById(taskId);
    }

    @Test
    void testUpdateTask() {
        Long taskId = 1L;
        LocalDateTime newDeadline = LocalDateTime.now().plusDays(1);
        Task existingTask = new Task(taskId, "Code refactor", "Java code refactor", LocalDateTime.now(), 1L);
        Task updatedTask = new Task(taskId, "Code refactor", "Python code refactor", newDeadline, 1L);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenReturn(updatedTask);

        Task result = taskService.updateTask(updatedTask);

        assertEquals(updatedTask, result);
        assertEquals(newDeadline, result.getDeadline());
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    void testUpdateTaskNotFound() {
        Long taskId = 1L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> taskService.updateTask(new Task()));
        verify(taskRepository, never()).save(any());
    }
}
