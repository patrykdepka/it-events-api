package pl.patrykdepka.iteventsapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static pl.patrykdepka.iteventsapi.security.JwtUtils.TOKEN_PREFIX;

@Component
public class ItEventsAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final ObjectMapper objectMapper;
    private final JwtUtils jwtUtils;

    public ItEventsAuthenticationSuccessHandler(ObjectMapper objectMapper, JwtUtils jwtUtils) {
        this.objectMapper = objectMapper;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON.toString());
        response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
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
