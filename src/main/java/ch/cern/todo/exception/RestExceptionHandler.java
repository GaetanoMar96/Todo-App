package ch.cern.todo.exception;

import jakarta.persistence.PersistenceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles BadRequestException. Created to encapsulate bad request errors.
     *
     * @param ex the BadRequestException
     * @return the ApiError object
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequest(BadRequestException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        apiError.setDebugMessage(getDebugMessageIfExists(ex));
        return buildResponseEntity(apiError);
    }

    /**
     * Handles PersistenceException. Created to encapsulate errors while performing sql operations with jpa.
     *
     * @param ex the PersistenceException
     * @return the ApiError object
     */
    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<Object> handleSqlConnection(PersistenceException ex) {
        ApiError apiError = new ApiError(INTERNAL_SERVER_ERROR);
        apiError.setMessage(ex.getMessage());
        apiError.setDebugMessage(getDebugMessageIfExists(ex));
        return buildResponseEntity(apiError);
    }

    /**
     * Handles DataNotFoundException. Created to encapsulate data not found errors.
     *
     * @param ex the DataNotFoundException
     * @return the ApiError object
     */
    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<Object> handleDataNotFound(DataNotFoundException ex) {
        ApiError apiError = new ApiError(NOT_FOUND);
        apiError.setMessage(ex.getMessage());
        apiError.setDebugMessage(getDebugMessageIfExists(ex));
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    private String getDebugMessageIfExists(Exception ex) {
        return ex.getCause() != null ? ex.getCause().getMessage() : "empty cause";
    }
}
