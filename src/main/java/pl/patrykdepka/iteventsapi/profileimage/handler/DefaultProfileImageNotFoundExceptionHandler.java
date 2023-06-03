package pl.patrykdepka.iteventsapi.profileimage.handler;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import pl.patrykdepka.iteventsapi.core.AbstractExceptionHandler;
import pl.patrykdepka.iteventsapi.profileimage.exception.DefaultProfileImageNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class DefaultProfileImageNotFoundExceptionHandler extends AbstractExceptionHandler {
    private static final String MESSAGE_CODE = "exception.DefaultProfileImageNotFoundException.message";

    public DefaultProfileImageNotFoundExceptionHandler(MessageSource messageSource) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, MESSAGE_CODE, messageSource, DefaultProfileImageNotFoundException.class);
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        response.setStatus(httpStatus.value());
        return getDefaultModelAndView();
    }
}
