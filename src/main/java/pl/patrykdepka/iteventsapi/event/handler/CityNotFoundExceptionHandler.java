package pl.patrykdepka.iteventsapi.event.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.patrykdepka.iteventsapi.core.AbstractExceptionHandler;
import pl.patrykdepka.iteventsapi.event.exception.CityNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestControllerAdvice
public class CityNotFoundExceptionHandler extends AbstractExceptionHandler {
    private static final String MESSAGE_CODE = "exception.CityNotFoundException.message";

    public CityNotFoundExceptionHandler(MessageSource messageSource, ObjectMapper objectMapper) {
        super(messageSource, MESSAGE_CODE, objectMapper);
    }

    @ExceptionHandler(CityNotFoundException.class)
    public void handle(HttpServletRequest request, HttpServletResponse response, Exception ex) throws IOException {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        writeErrorAsJson(request, response, ex);
    }
}
