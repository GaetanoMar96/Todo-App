package ch.cern.todo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class DataNotFoundException extends ResponseStatusException {
    private final String message;
    public DataNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND);
        this.message = message;
    }
}
