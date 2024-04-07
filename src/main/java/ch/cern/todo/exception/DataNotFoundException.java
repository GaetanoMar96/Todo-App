package ch.cern.todo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
public class DataNotFoundException extends ResponseStatusException {
    private final String message;
    public DataNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND);
        this.message = message;
    }
}
