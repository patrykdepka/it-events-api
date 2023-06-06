package pl.patrykdepka.iteventsapi.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.patrykdepka.iteventsapi.appuser.exception.AppUserNotFoundException;
import pl.patrykdepka.iteventsapi.security.ErrorResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ItEventsExceptionHandler {
    private final ObjectMapper objectMapper;

    public ItEventsExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
    }

    @ExceptionHandler(AppUserNotFoundException.class)
    public void handleAppUserNotFoundException(HttpServletRequest request, HttpServletResponse response, AppUserNotFoundException ex) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON.toString());
        response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        response.setStatus(HttpStatus.NOT_FOUND.value());
        ErrorResponse errorResponse = new ErrorResponse(
                new Date(),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getServletPath()
        );
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
