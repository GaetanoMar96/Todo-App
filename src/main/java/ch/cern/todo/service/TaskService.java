package ch.cern.todo.service;

import ch.cern.todo.exception.BadRequestException;
import ch.cern.todo.exception.DataNotFoundException;
import ch.cern.todo.model.Category;
import ch.cern.todo.model.Task;
import ch.cern.todo.repository.CategoryRepository;
import ch.cern.todo.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public void createTask(Task task) {
        //Check if the deadline is after today date
        if (task.getDeadline() != null && task.getDeadline().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("The deadline is before today date");
        }
        // Check if the category ID exists in the Category table
        Optional<Category> categoryOptional = categoryRepository.findById(task.getCategoryId());
        if (categoryOptional.isPresent()) {
            taskRepository.save(task);
            return;
        }
        // The category ID does not exist
        throw new BadRequestException("The category id is not present");
    }

    public boolean deleteTaskById(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false; // Task with given id doesn't exist
    }

    public Task updateTask(Task updatedTask) {
        Optional<Task> existingTaskOptional = taskRepository.findById(updatedTask.getTaskId());
        if (existingTaskOptional.isPresent()) {
            Task existingTask = existingTaskOptional.get();
            if (StringUtils.hasText(updatedTask.getDescription())) { //only if new description has text
                existingTask.setDescription(updatedTask.getDescription()); //update description
            }
            existingTask.setDeadline(updatedTask.getDeadline()); //update deadline
            return taskRepository.save(existingTask);
        }
        throw new DataNotFoundException("Unable to find the requested task");
    }
}
