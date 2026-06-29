package sube.interviews.mareoenvios.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND,  ex.getMessage());
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessRule(BusinessRuleException ex) {
        return buildErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    @ExceptionHandler(FailedDependencyException.class)
    public ResponseEntity<Map<String, Object>> handleFailedDependency(FailedDependencyException ex) {
        return buildErrorResponse(HttpStatus.FAILED_DEPENDENCY, ex.getMessage());
    }

    @ExceptionHandler(InvalidStateTransitionException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidState(InvalidStateTransitionException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Faltan campos obligatorios en la petición");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericError(Exception ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = String.format("El parámetro '%s' debe ser de tipo %s",
                ex.getName(), ex.getRequiredType().getSimpleName());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleMalformedJson(HttpMessageNotReadableException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "El cuerpo de la petición (JSON) está mal formado o contiene tipos de datos inválidos");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        String message = String.format("El método HTTP '%s' no está soportado para este endpoint. Métodos permitidos: %s",
                ex.getMethod(), ex.getSupportedHttpMethods());
        return buildErrorResponse(HttpStatus.METHOD_NOT_ALLOWED, message);
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", status.value());
        body.put("message", message);
        log.error("Error response: {}", body);
        return new ResponseEntity<>(body, status);
    }
}
