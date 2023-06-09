package pl.patrykdepka.iteventsapi.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Locale;

public abstract class AbstractExceptionHandler {
    private final Log log = LogFactory.getLog(AbstractExceptionHandler.class);
    private final MessageSource messageSource;
    private final String messageCode;
    private final ObjectMapper objectMapper;

    public AbstractExceptionHandler(MessageSource messageSource, String messageCode, ObjectMapper objectMapper) {
        this.messageSource = messageSource;
        this.messageCode = messageCode;
        this.objectMapper = objectMapper;
    }

    public void writeErrorAsJson(HttpServletRequest request, HttpServletResponse response, Exception ex) throws IOException {
        log.error(ex.getMessage());
        response.setContentType(MediaType.APPLICATION_JSON.toString());
        response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        ErrorResponse errorResponse = new ErrorResponse(
                new Date(),
                getStatus(response).value(),
                getStatus(response).getReasonPhrase(),
                getErrorMessage(),
                request.getServletPath()
        );
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }

    private HttpStatus getStatus(HttpServletResponse response) {
        return HttpStatus.valueOf(response.getStatus());
    }

    private String getErrorMessage() {
        return messageSource.getMessage(messageCode, null, Locale.getDefault());
    }
}
