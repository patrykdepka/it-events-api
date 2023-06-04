package pl.patrykdepka.iteventsapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static pl.patrykdepka.iteventsapi.security.JwtUtils.TOKEN_PREFIX;

@Component
@RequiredArgsConstructor
public class ItEventsAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final ObjectMapper objectMapper;
    private final JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        response.getWriter().write(objectMapper.writeValueAsString(
                new AuthenticationSuccessResponse(TOKEN_PREFIX + jwtUtils.generateToken(userDetails.getUsername())))
        );
    }

    @Getter
    private static class AuthenticationSuccessResponse {
        private final String token;

        public AuthenticationSuccessResponse(String token) {
            this.token = token;
        }
    }
}
