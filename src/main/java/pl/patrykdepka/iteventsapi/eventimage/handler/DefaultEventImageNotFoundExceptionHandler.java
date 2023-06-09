package pl.patrykdepka.iteventsapi.eventimage.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.patrykdepka.iteventsapi.core.AbstractExceptionHandler;
import pl.patrykdepka.iteventsapi.eventimage.exception.DefaultEventImageNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestControllerAdvice
public class DefaultEventImageNotFoundExceptionHandler extends AbstractExceptionHandler {
    private static final String MESSAGE_CODE = "exception.DefaultEventImageNotFoundException.message";

    public DefaultEventImageNotFoundExceptionHandler(MessageSource messageSource, ObjectMapper objectMapper) {
        super(messageSource, MESSAGE_CODE, objectMapper);
    }

    @ExceptionHandler(DefaultEventImageNotFoundException.class)
    public void handle(HttpServletRequest request, HttpServletResponse response, Exception ex) throws IOException {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        writeErrorAsJson(request, response, ex);
    }
}
