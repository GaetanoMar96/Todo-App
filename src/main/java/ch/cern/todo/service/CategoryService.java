package ch.cern.todo.service;

import ch.cern.todo.exception.BadRequestException;
import ch.cern.todo.exception.DataNotFoundException;
import ch.cern.todo.model.Category;
import ch.cern.todo.repository.CategoryRepository;
import ch.cern.todo.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final TaskRepository taskRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public void createCategory(Category category) {
        categoryRepository.save(category);
    }

    public void deleteCategoryIfNotReferenced(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            if (taskRepository.existsByCategoryId(id)) {
                throw new BadRequestException("Category is referenced by tasks and cannot be deleted.");
            }
            categoryRepository.deleteById(id);
            return;
        }
        throw new DataNotFoundException("Category with given id does not exist.");
    }
}
