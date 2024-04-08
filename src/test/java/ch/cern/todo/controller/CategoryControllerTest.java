package ch.cern.todo.controller;

import ch.cern.todo.BaseIntegrationTest;
import ch.cern.todo.model.Category;
import ch.cern.todo.repository.CategoryRepository;
import ch.cern.todo.utils.Factory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CategoryControllerTest extends BaseIntegrationTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void getAllCategoriesTest_StatusOk() throws Exception {
        mockMvc.perform(get("/api/v1/todo/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("development"))
                .andExpect(jsonPath("$[0].description").value("software development"))
                .andExpect(jsonPath("$[1].name").value("testing"))
                .andExpect(jsonPath("$[1].description").value("unit tests"));
    }

    @Test
    void getCategoryByIdTest_StatusOk() throws Exception {
        mockMvc.perform(get("/api/v1/todo/categories/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("development"))
                .andExpect(jsonPath("$.description").value("software development"));
    }

    @Test
    void getCategoryByIdTest_StatusNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/todo/categories/{id}", 6)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void createCategory_StatusCreated() throws Exception {
        Category category = Factory.getCategory("code review", "java code review");

        mockMvc.perform(post("/api/v1/todo/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(category))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Optional<Category> categoryOptional = categoryRepository.findById(3L);
        assertTrue(categoryOptional.isPresent());
        assertEquals(3L, categoryOptional.get().getId());
    }

    @Test
    void createCategory_StatusBadRequest() throws Exception {
        Category category = Factory.getCategory("testing", "integration testing");
        //name already present should cause data integrity violation

        mockMvc.perform(post("/api/v1/todo/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(category))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        //assert that category was not inserted
        Optional<Category> categoryOptional = categoryRepository.findById(3L);
        assertFalse(categoryOptional.isPresent());
    }

    @Test
    void deleteCategoryByIdTest_StatusBadRequest() throws Exception {
        mockMvc.perform(delete("/api/v1/todo/categories/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    void deleteCategoryByIdTest_StatusOk() throws Exception {
        mockMvc.perform(delete("/api/v1/todo/categories/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        List<Category> categoryList = categoryRepository.findAll();
        assertEquals(1, categoryList.size());
    }

    @Test
    void deleteCategoryByIdTest_StatusNotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/todo/categories/{id}", 6)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
