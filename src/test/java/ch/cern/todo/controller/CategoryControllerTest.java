package ch.cern.todo.controller;

import ch.cern.todo.BaseIntegrationTest;
import ch.cern.todo.model.Category;
import ch.cern.todo.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql(scripts = {"classpath:/scripts/create-h2.sql", "classpath:/scripts/insert-h2.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:/scripts/drop-h2.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CategoryControllerTest extends BaseIntegrationTest {

    @Autowired
    private CategoryRepository categoryRepository;
    //TODO add JSON pATH matcher
    @Test
    void getAllCategoriesTest_StatusOk() throws Exception {
        mockMvc.perform(get("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

    }

    @Test
    void getCategoryByIdTest_StatusOk() throws Exception {
        mockMvc.perform(get("/api/v1/categories/{id}", 4)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

    }

    @Test
    void getCategoryByIdTest_StatusNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/categories/{id}", 6)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void createCategory_StatusCreated() throws Exception {
        Category category = new Category();
        category.setName("testing");
        category.setDescription("integration testing");
        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(category))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Optional<Category> categoryOptional = categoryRepository.findById(1L);
        assertTrue(categoryOptional.isPresent());
        assertEquals(1L, categoryOptional.get().getId());
    }

    @Test
    void deleteCategoryByIdTest_StatusBadRequest() throws Exception {
        mockMvc.perform(delete("/api/v1/categories/{id}", 4)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    void deleteCategoryByIdTest_StatusOk() throws Exception {
        mockMvc.perform(delete("/api/v1/categories/{id}", 5)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCategoryByIdTest_StatusNotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/categories/{id}", 6)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
