package pl.patrykdepka.iteventsapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

import static pl.patrykdepka.iteventsapi.appuser.model.Role.*;

@Configuration
public class SecurityConfig {
    private final DataSource dataSource;

    public SecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.authorizeRequests(request -> request
                .antMatchers("/h2-console/**").permitAll()
                .mvcMatchers("/").permitAll()
                .mvcMatchers("/login**").permitAll()
                .mvcMatchers("/register", "/confirmation").permitAll()
                .mvcMatchers("/users").hasRole(ROLE_USER.getRole())
                .mvcMatchers("/admin/**").hasRole(ROLE_ADMIN.getRole())
                .mvcMatchers("/events").permitAll()
                .mvcMatchers("/events/cities/*").permitAll()
                .mvcMatchers("/archive/events").permitAll()
                .mvcMatchers("/archive/events/cities/*").permitAll()
                .mvcMatchers("/organizer/create_event").hasRole(ROLE_ORGANIZER.getRole())
                .anyRequest().authenticated());
        http.formLogin(login -> login
                .loginPage("/login").permitAll()
                .failureHandler(new ItEventsAuthenticationFailureHandler())
        );
        http.logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout/**", HttpMethod.GET.name()))
                .logoutSuccessUrl("/login?logout").permitAll()
        );
        http.rememberMe().tokenRepository(persistentTokenRepository());
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().mvcMatchers(
                "/scripts/**", "/styles/**"
        );
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new ItEventsAccessDeniedHandler();
    }
}
