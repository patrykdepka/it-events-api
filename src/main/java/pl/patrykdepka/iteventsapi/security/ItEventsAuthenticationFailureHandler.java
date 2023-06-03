package pl.patrykdepka.iteventsapi.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ItEventsAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String redirectUrl = getRedirectUrl(exception);
        super.setDefaultFailureUrl(redirectUrl);
        super.onAuthenticationFailure(request, response, exception);
    }

    private String getRedirectUrl(AuthenticationException exception) {
        if (exception instanceof BadCredentialsException) {
            return "/login?error=badCredentials";
        } else if (exception instanceof DisabledException) {
            return "/login?error=disabledAccount";
        } else if (exception instanceof LockedException) {
            return "/login?error=lockedAccount";
        } else {
            return "/login?error=unknownError";
        }
    }
}
