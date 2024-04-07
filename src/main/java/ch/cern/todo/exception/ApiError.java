package ch.cern.todo.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiError {

    private HttpStatus status;
    private String message;
    private String debugMessage;

    public ApiError(HttpStatus status) {
        this.status = status;
    }

    public ApiError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
