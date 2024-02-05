package pl.patrykdepka.iteventsapi.appuser.domain.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.patrykdepka.iteventsapi.appuser.domain.exception.IncorrectCurrentPasswordException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
public class IncorrectCurrentPasswordExceptionHandler {
    private final MessageSource messageSource;

    private static final String MESSAGE_CODE = "exception.IncorrectCurrentPasswordException.message";

    @ExceptionHandler(IncorrectCurrentPasswordException.class)
    public Map<String, String> handle(HttpServletRequest request, HttpServletResponse response, IncorrectCurrentPasswordException ex) {
        return Map.of(
                ex.getFieldName(),
                messageSource.getMessage(
                        MESSAGE_CODE,
                        null,
                        Locale.getDefault()
                )
        );
    }
}
