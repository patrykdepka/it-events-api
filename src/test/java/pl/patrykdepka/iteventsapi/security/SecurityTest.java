package pl.patrykdepka.iteventsapi.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import pl.patrykdepka.iteventsapi.core.BaseControllerIT;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class SecurityTest extends BaseControllerIT {

    @Test
    void shouldReturn401ResponseCodeWhenUnauthorizedUserGetsUsersByAdminPanel() {
        // when
        ResponseEntity<Map<String, String>> response = restTemplate.exchange(
                "/api/v1/admin/users",
                GET,
                sendRequestAsUnauthorizedUser(null),
                new ParameterizedTypeReference<>() {}
        );
        // then
        assertThat(response.getStatusCode()).isEqualTo(UNAUTHORIZED);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void shouldReturn403ResponseCodeWhenUserHasNoAdminRoleAndGetsUsersByAdminPanel(int number) {
        // given
        List<HttpEntity<String>> users = List.of(
                sendRequestAsUser(null),
                sendRequestAsOrganizer(null)
        );
        // when
        ResponseEntity<Map<String, String>> response = restTemplate.exchange(
                "/api/v1/admin/users",
                GET,
                users.get(number),
                new ParameterizedTypeReference<>() {}
        );
        // then
        assertThat(response.getStatusCode()).isEqualTo(FORBIDDEN);
    }
}
