package pl.patrykdepka.iteventsapi.appuser.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.patrykdepka.iteventsapi.appuser.exception.AppUserNotFoundException;
import pl.patrykdepka.iteventsapi.core.AbstractExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestControllerAdvice
public class AppUserNotFoundExceptionHandler extends AbstractExceptionHandler {
    private static final String MESSAGE_CODE = "exception.AppUserNotFoundException.message";

    public AppUserNotFoundExceptionHandler(MessageSource messageSource, ObjectMapper objectMapper) {
        super(messageSource, MESSAGE_CODE, objectMapper);
    }

    @ExceptionHandler(AppUserNotFoundException.class)
    public void handle(HttpServletRequest request, HttpServletResponse response, Exception ex) throws IOException {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        writeErrorAsJson(request, response, ex);
    }
}
