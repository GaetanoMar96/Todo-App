package ch.cern.todo.controller;

import ch.cern.todo.model.Task;
import ch.cern.todo.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/todo/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Optional<Task> task = taskService.getTaskById(id);
        return task.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Void> createTask(@RequestBody Task task) {
        taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteTaskById(@PathVariable Long id) {
        if (taskService.deleteTaskById(id)) {
            return ResponseEntity.ok(id);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping
    public ResponseEntity<Task> updateTask(@RequestBody Task task) {
        if (task == null || task.getTaskId() == null || task.getDeadline() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        if (task.getDeadline().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body(null);
        }
        Task newTask = taskService.updateTask(task);
        return ResponseEntity.status(HttpStatus.OK).body(newTask);
    }
}
