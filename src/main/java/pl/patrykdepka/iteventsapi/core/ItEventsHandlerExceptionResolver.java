package pl.patrykdepka.iteventsapi.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Locale;

@Component
public class ItEventsHandlerExceptionResolver extends AbstractHandlerExceptionResolver {
    private final Log log = LogFactory.getLog(ItEventsHandlerExceptionResolver.class);
    private final MessageSource messageSource;
    private final List<ExceptionHandler> handlers;

    public ItEventsHandlerExceptionResolver(MessageSource messageSource, List<ExceptionHandler> handlers) {
        this.messageSource = messageSource;
        this.handlers = handlers;
    }

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.error(ex.getMessage());
        return handlers
                .stream()
                .filter(han -> han.canBeAssign(ex))
                .findFirst()
                .map(han -> han.handle(request, response, ex))
                .orElseGet(() -> handleOtherException(response, ex));
    }

    private ModelAndView handleOtherException(HttpServletResponse response, Exception ex) {
        log.error("Unhandled exception: [name=" + ex.getClass().getName() + ", message=" + ex.getMessage() + "]");
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("httpStatus", response.getStatus());
        String errorMessage = messageSource.getMessage("exception.otherException.message", null, Locale.getDefault());
        modelAndView.addObject("errorMessage", errorMessage);
        modelAndView.setViewName("error");
        return modelAndView;
    }
}
