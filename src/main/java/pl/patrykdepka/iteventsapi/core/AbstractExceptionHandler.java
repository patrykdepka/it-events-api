package pl.patrykdepka.iteventsapi.core;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.Locale;

public abstract class AbstractExceptionHandler implements ExceptionHandler {
    protected final HttpStatus httpStatus;
    private final String messageCode;
    private final MessageSource messageSource;
    private final Class<? extends Exception> exceptionClass;

    public AbstractExceptionHandler(
            HttpStatus httpStatus,
            String messageCode,
            MessageSource messageSource,
            Class<? extends Exception> exceptionClass
    ) {
        this.httpStatus = httpStatus;
        this.messageCode = messageCode;
        this.messageSource = messageSource;
        this.exceptionClass = exceptionClass;
    }

    @Override
    public boolean canBeAssign(Exception exception) {
        return exceptionClass.isInstance(exception);
    }

    protected ModelAndView getDefaultModelAndView() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("httpStatus", httpStatus.value());
        modelAndView.addObject("errorMessage", getErrorMessage());
        modelAndView.setViewName("error");
        return modelAndView;
    }

    private String getErrorMessage() {
        return messageSource.getMessage(messageCode, null, Locale.getDefault());
    }
}
