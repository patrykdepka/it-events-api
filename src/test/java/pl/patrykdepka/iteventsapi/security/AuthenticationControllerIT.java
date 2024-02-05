package pl.patrykdepka.iteventsapi.security;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import pl.patrykdepka.iteventsapi.core.BaseControllerIT;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthenticationControllerIT extends BaseControllerIT {

    @Test
    void shouldReturnErrorWhenEmailIsInvalid() throws Exception {
        // given
        var loginData = new LoginData("testowyuzytkownik@example.pl", "qwerty");
        // then
        mockMvc.perform(post("/api/v1/auth/signin")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.convertValue(loginData, JsonNode.class).toString())
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnErrorWhenPasswordIsInvalid() throws Exception {
        // given
        var loginData = new LoginData("testowyuzytkownik@example.com", "ytrewq");
        // then
        mockMvc.perform(post("/api/v1/auth/signin")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.convertValue(loginData, JsonNode.class).toString())
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldLogInUserAndReturnToken() {
        // when
        ResponseEntity<Map<String, String>> response = restTemplate.exchange(
                "/api/v1/auth/signin",
                POST,
                sendRequestAsUnauthorizedUser(new LoginData("user@example.com", "qwerty")),
                new ParameterizedTypeReference<>() {}
        );
        // then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("token").startsWith("Bearer ")).isTrue();
    }
}
