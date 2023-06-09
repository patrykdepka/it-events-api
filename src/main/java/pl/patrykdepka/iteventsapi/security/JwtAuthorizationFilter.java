package pl.patrykdepka.iteventsapi.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static pl.patrykdepka.iteventsapi.security.JwtUtils.TOKEN_PREFIX;

public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    public JwtAuthorizationFilter(JwtUtils jwtUtils, UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().contains("/api/v1/auth/signin")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            String token = authorizationHeader.substring(TOKEN_PREFIX.length());
            try {
                if (jwtUtils.validateToken(token)) {
                    String username = jwtUtils.extractUsername(token);
                    if (StringUtils.hasText(username)) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            } catch (JWTVerificationException ex) {
                logger.error("Token is incorrect or expired");
            } catch (UsernameNotFoundException ex) {
                logger.error(ex.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}
