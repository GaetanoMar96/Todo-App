package ch.cern.todo.service;

import ch.cern.todo.exception.BadRequestException;
import ch.cern.todo.exception.DataNotFoundException;
import ch.cern.todo.model.Category;
import ch.cern.todo.repository.CategoryRepository;
import ch.cern.todo.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private TaskRepository taskRepository;

    @Test
    void testGetAllCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1L, "Development", "java development"));
        categories.add(new Category(2L, "Testing", "integration testing"));

        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> result = categoryService.getAllCategories();

        assertEquals(categories.size(), result.size());
        assertArrayEquals(categories.toArray(), result.toArray());
    }

    @Test
    void testGetCategoryById() {
        Category category = new Category(1L, "Development", "java development");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Optional<Category> result = categoryService.getCategoryById(1L);
        assertTrue(result.isPresent());
        assertEquals(category, result.get());
    }

    @Test
    void testCreateCategory() {
        Category category = new Category(1L, "Development", "java development");
        categoryService.createCategory(category);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void testDeleteCategoryIfNotReferenced() {
        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(new Category()));
        when(taskRepository.existsByCategoryId(categoryId)).thenReturn(false);

        categoryService.deleteCategoryIfNotReferenced(categoryId);
        verify(categoryRepository, times(1)).deleteById(categoryId);
    }

    @Test
    void testDeleteCategoryIfReferenced() {
        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(new Category()));
        when(taskRepository.existsByCategoryId(categoryId)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> categoryService.deleteCategoryIfNotReferenced(categoryId));
        verify(categoryRepository, never()).deleteById(categoryId);
    }

    @Test
    void testDeleteCategoryNotFound() {
        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> categoryService.deleteCategoryIfNotReferenced(categoryId));
        verify(categoryRepository, never()).deleteById(categoryId);
    }
}
