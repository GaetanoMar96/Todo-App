package ch.cern.todo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BadRequestException extends ResponseStatusException {
    private final String message;
    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST);
        this.message = message;
    }
}
