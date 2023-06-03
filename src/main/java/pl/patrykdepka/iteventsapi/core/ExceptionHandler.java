package pl.patrykdepka.iteventsapi.core;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ExceptionHandler {

    ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Exception ex);

    boolean canBeAssign(Exception exception);
}
