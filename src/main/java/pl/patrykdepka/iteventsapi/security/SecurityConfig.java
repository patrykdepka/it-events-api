package pl.patrykdepka.iteventsapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static pl.patrykdepka.iteventsapi.appuser.domain.Role.*;

@Configuration
public class SecurityConfig {
    private final ObjectMapper objectMapper;
    private final ItEventsAuthenticationSuccessHandler successHandler;
    private final ItEventsAuthenticationFailureHandler failureHandler;
    private final AppUserDetailsService appUserDetailsService;
    private final JwtUtils jwtUtils;
    private final ItEventsAuthenticationEntryPoint itEventsAuthenticationEntryPoint;

    public SecurityConfig(
            ObjectMapper objectMapper,
            ItEventsAuthenticationSuccessHandler successHandler,
            ItEventsAuthenticationFailureHandler failureHandler,
            AppUserDetailsService appUserDetailsService,
            JwtUtils jwtUtils,
            ItEventsAuthenticationEntryPoint itEventsAuthenticationEntryPoint
    ) {
        this.objectMapper = objectMapper;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.appUserDetailsService = appUserDetailsService;
        this.jwtUtils = jwtUtils;
        this.itEventsAuthenticationEntryPoint = itEventsAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests(request -> request
                .antMatchers("/v3/api-docs", "/v3/api-docs/**").permitAll()
                .antMatchers("/swagger-ui/**", "/swagger-ui.html").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/api/v1/home").permitAll()
                .antMatchers("/api/v1/auth/signin", HttpMethod.POST.name()).permitAll()
                .antMatchers("/api/v1/register").permitAll()
                .antMatchers("/api/v1/users").hasRole(ROLE_USER.getRole())
                .antMatchers("/api/v1/admin/**").hasRole(ROLE_ADMIN.getRole())
                .antMatchers("/api/v1/events").permitAll()
                .antMatchers("/api/v1/events/cities/*").permitAll()
                .antMatchers("/api/v1/archive/events").permitAll()
                .antMatchers("/api/v1/archive/events/cities/*").permitAll()
                .antMatchers("/api/v1/organizer/create_event").hasRole(ROLE_ORGANIZER.getRole())
                .anyRequest().authenticated());
        http.addFilter(authenticationFilter(authenticationManager));
        http.addFilterBefore(new JwtAuthorizationFilter(jwtUtils, appUserDetailsService), JsonObjectAuthenticationFilter.class);
        http.exceptionHandling().authenticationEntryPoint(itEventsAuthenticationEntryPoint);
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    public JsonObjectAuthenticationFilter authenticationFilter(AuthenticationManager authenticationManager) {
        JsonObjectAuthenticationFilter authenticationFilter = new JsonObjectAuthenticationFilter(objectMapper);
        authenticationFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/v1/auth/signin", "POST"));
        authenticationFilter.setAuthenticationManager(authenticationManager);
        authenticationFilter.setAuthenticationSuccessHandler(successHandler);
        authenticationFilter.setAuthenticationFailureHandler(failureHandler);
        return authenticationFilter;
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new ItEventsAccessDeniedHandler(objectMapper);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
