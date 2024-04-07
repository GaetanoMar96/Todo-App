package ch.cern.todo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Sql(scripts = {"classpath:/scripts/create-h2.sql", "classpath:/scripts/insert-h2.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:/scripts/drop-h2.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public abstract class BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    protected ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

}
