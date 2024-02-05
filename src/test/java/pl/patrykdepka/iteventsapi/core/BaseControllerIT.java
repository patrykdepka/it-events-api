package pl.patrykdepka.iteventsapi.core;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import pl.patrykdepka.iteventsapi.appuser.domain.AppUser;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@AutoConfigureMockMvc
public class BaseControllerIT extends BaseIT {
    private static final String TOKEN_PREFIX = "Bearer ";

    @Value("${application.security.jwt.secret}")
    private String secret;
    @Autowired
    protected TestRestTemplate restTemplate;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected MockMvc mockMvc;

    public HttpEntity<String> sendRequestAsUnauthorizedUser(Object object) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);

        if (object != null) {
            JsonNode json = objectMapper.convertValue(object, JsonNode.class);
            return new HttpEntity<>(json.toString(), headers);
        }

        return new HttpEntity<>(headers);
    }

    public HttpEntity<String> sendRequestAsUser(Object object) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        AppUser user = appUserRepository.findByEmail("user@example.com").orElseThrow();
        headers.add(AUTHORIZATION, TOKEN_PREFIX + generateToken(user.getEmail()));

        if (object != null) {
            JsonNode json = objectMapper.convertValue(object, JsonNode.class);
            return new HttpEntity<>(json.toString(), headers);
        }

        return new HttpEntity<>(headers);
    }

    public HttpEntity<String> sendRequestAsEventParticipant(Object object) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        AppUser user = appUserRepository.findByEmail("patrykkowalski@example.com").orElseThrow();
        headers.add(AUTHORIZATION, TOKEN_PREFIX + generateToken(user.getEmail()));

        if (object != null) {
            JsonNode json = objectMapper.convertValue(object, JsonNode.class);
            return new HttpEntity<>(json.toString(), headers);
        }

        return new HttpEntity<>(headers);
    }

    public HttpEntity<String> sendRequestAsOrganizer(Object object) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        AppUser admin = appUserRepository.findByEmail("organizer@example.com").orElseThrow();
        headers.add(AUTHORIZATION, TOKEN_PREFIX + generateToken(admin.getEmail()));

        if (object != null) {
            JsonNode json = objectMapper.convertValue(object, JsonNode.class);
            return new HttpEntity<>(json.toString(), headers);
        }

        return new HttpEntity<>(headers);
    }

    public HttpEntity<String> sendRequestAsAdmin(Object object) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        AppUser admin = appUserRepository.findByEmail("admin@example.com").orElseThrow();
        headers.add(AUTHORIZATION, TOKEN_PREFIX + generateToken(admin.getEmail()));

        if (object != null) {
            JsonNode json = objectMapper.convertValue(object, JsonNode.class);
            return new HttpEntity<>(json.toString(), headers);
        }

        return new HttpEntity<>(headers);
    }

    private String generateToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1L)))
                .sign(Algorithm.HMAC256(secret));
    }
}
