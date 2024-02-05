package pl.patrykdepka.iteventsapi.appuser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.AppUserPasswordEditDTO;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.AppUserProfileDTO;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.AppUserProfileEditDTO;
import pl.patrykdepka.iteventsapi.core.BaseControllerIT;
import pl.patrykdepka.iteventsapi.core.TestPageImpl;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;
import static pl.patrykdepka.iteventsapi.creator.AppUserProfileEditDTOCreator.createNewProfileData;
import static pl.patrykdepka.iteventsapi.creator.CreateUserDTOCreator.createNewUserData;

class AppUserControllerIT extends BaseControllerIT {

    @Test
    void shouldReturnErrorWhenUserWithGivenEmailExists() {
        // given
        var newUserData = createNewUserData(
                "Patryk", "Kowalski", "1995-10-06", "qwerty", "qwerty"
        );
        // when
        ResponseEntity<Map<String, String>> response = restTemplate.exchange(
                "/api/v1/register",
                POST,
                sendRequestAsUnauthorizedUser(newUserData),
                new ParameterizedTypeReference<>() {}
        );
        // then
        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(1);
        assertThat(response.getBody().get("email")).isEqualTo("{form.field.email.error.emailIsInUse.message}");
    }

    @Test
    void shouldCreateUserWhenUserWithGivenEmailDoesNotExist() {
        // given
        var newUserData = createNewUserData(
                "Patryk", "Testowy", "1995-10-06", "qwerty", "qwerty"
        );
        // when
        ResponseEntity<AppUserProfileDTO> response = restTemplate.exchange(
                "/api/v1/register",
                POST,
                sendRequestAsUnauthorizedUser(newUserData),
                AppUserProfileDTO.class
        );
        // then
        assertThat(response.getStatusCode()).isEqualTo(CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getContentType()).isEqualTo("image/png");
        assertThat(response.getBody().getProfileImageData()).isNotBlank();
        assertThat(response.getBody().getFirstName()).isEqualTo(newUserData.getFirstName());
        assertThat(response.getBody().getLastName()).isEqualTo(newUserData.getLastName());
        assertThat(response.getBody().getDateOfBirth()).isEqualTo(newUserData.getDateOfBirth());
        assertThat(response.getBody().getCity()).isBlank();
        assertThat(response.getBody().getBio()).isBlank();
    }

    @Test
    void shouldReturnProfileForUserWithGivenId() {
        // given
        var id = 5L;
        // when
        ResponseEntity<AppUserProfileDTO> response = restTemplate.exchange(
                "/api/v1/users/" + id + "/profile/view",
                GET,
                sendRequestAsUser(null),
                AppUserProfileDTO.class
        );
        // then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(id);
        assertThat(response.getBody().getContentType()).isEqualTo("image/png");
        assertThat(response.getBody().getProfileImageData()).isNotBlank();
        assertThat(response.getBody().getFirstName()).isEqualTo("Patryk");
        assertThat(response.getBody().getLastName()).isEqualTo("Kowalski");
        assertThat(response.getBody().getDateOfBirth()).isEqualTo("1995-10-06");
        assertThat(response.getBody().getCity()).isEqualTo("Rzeszów");
        assertThat(response.getBody().getBio()).isEqualTo("Cześć! Nazywam się Patryk Kowalski i mieszkam w Rzeszowie. Jestem absolwentem informatyki na WSIiZ.");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    void shouldReturnUsers(int pageNumber) {
        // when
        ResponseEntity<TestPageImpl<AppUserProfileDTO>> response = restTemplate.exchange(
                "/api/v1/users?page=" + pageNumber,
                GET,
                sendRequestAsUser(null),
                new ParameterizedTypeReference<>() {}
        );
        // then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalPages()).isEqualTo(2);
        assertThat(response.getBody().getTotalElements()).isEqualTo(15);
        assertThat(response.getBody().getNumber()).isEqualTo(pageNumber - 1);
    }

    @Test
    void shouldReturnUsersWithGivenLastName() {
        // when
        ResponseEntity<TestPageImpl<AppUserProfileDTO>> response = restTemplate.exchange(
                fromUriString("/api/v1/users/results").queryParam("search_query", "kowalski").toUriString(),
                GET,
                sendRequestAsUser(null),
                new ParameterizedTypeReference<>() {}
        );
        // then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalPages()).isEqualTo(1);
        assertThat(response.getBody().getTotalElements()).isEqualTo(3);
        assertThat(response.getBody().getNumber()).isEqualTo(0);
        response.getBody().forEach(u -> assertThat(u.getLastName()).isEqualTo("Kowalski"));
    }

    @Test
    void shouldReturnUsersWithGivenFirstNameAndLastName() {
        // when
        ResponseEntity<TestPageImpl<AppUserProfileDTO>> response = restTemplate.exchange(
                fromUriString("/api/v1/users/results").queryParam("search_query", "patryk+kowalski").toUriString(),
                GET,
                sendRequestAsUser(null),
                new ParameterizedTypeReference<>() {}
        );
        // then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalPages()).isEqualTo(1);
        assertThat(response.getBody().getTotalElements()).isEqualTo(1);
        assertThat(response.getBody().getNumber()).isEqualTo(0);
        assertThat(response.getBody().getContent().get(0).getFirstName()).isEqualTo("Patryk");
        assertThat(response.getBody().getContent().get(0).getLastName()).isEqualTo("Kowalski");
    }

    @Test
    void shouldReturnProfileForCurrentLoggedUser() {
        // when
        ResponseEntity<AppUserProfileDTO> response = restTemplate.exchange(
                "/api/v1/users/me/profile/view",
                GET,
                sendRequestAsUser(null),
                AppUserProfileDTO.class
        );
        // then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(3L);
        assertThat(response.getBody().getContentType()).isEqualTo("image/png");
        assertThat(response.getBody().getProfileImageData()).isNotBlank();
        assertThat(response.getBody().getFirstName()).isEqualTo("User");
        assertThat(response.getBody().getLastName()).isEqualTo("User");
        assertThat(response.getBody().getDateOfBirth()).isEqualTo("1990-01-01");
        assertThat(response.getBody().getCity()).isEqualTo("Rzeszów");
        assertThat(response.getBody().getBio()).isEqualTo("Cześć! Jestem domyślnym użytkownikiem tego serwisu.");
    }

    @Test
    void shouldReturnProfileDataForCurrentLoggedUser() {
        // when
        ResponseEntity<AppUserProfileEditDTO> response = restTemplate.exchange(
                "/api/v1/users/me/profile",
                GET,
                sendRequestAsUser(null),
                AppUserProfileEditDTO.class
        );
        // then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCurrentProfileImageData()).isNotBlank();
        assertThat(response.getBody().getProfileImage()).isNull();
        assertThat(response.getBody().getFirstName()).isEqualTo("User");
        assertThat(response.getBody().getLastName()).isEqualTo("User");
        assertThat(response.getBody().getDateOfBirth()).isEqualTo("1990-01-01");
        assertThat(response.getBody().getCity()).isEqualTo("Rzeszów");
        assertThat(response.getBody().getBio()).isEqualTo("Cześć! Jestem domyślnym użytkownikiem tego serwisu.");
    }

    @Test
    void shouldUpdateAndReturnUpdatedProfileDataForCurrentLoggedUser() {
        // given
        var newProfileData = createNewProfileData(null, "Testy Wielkie", "Testowy biogram");
        // when
        ResponseEntity<AppUserProfileEditDTO> response = restTemplate.exchange(
                "/api/v1/users/me/profile",
                PUT,
                sendRequestAsUser(newProfileData),
                AppUserProfileEditDTO.class
        );
        // then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCurrentProfileImageData()).isNotBlank();
        assertThat(response.getBody().getProfileImage()).isNull();
        assertThat(response.getBody().getFirstName()).isEqualTo("User");
        assertThat(response.getBody().getLastName()).isEqualTo("User");
        assertThat(response.getBody().getDateOfBirth()).isEqualTo("1990-01-01");
        assertThat(response.getBody().getCity()).isEqualTo(newProfileData.getCity());
        assertThat(response.getBody().getBio()).isEqualTo(newProfileData.getBio());
    }

    @Test
    void shouldUpdatePasswordForCurrentLoggedUser() {
        // given
        var newPasswordData = new AppUserPasswordEditDTO(
                "qwerty", "ytrewq", "ytrewq"
        );
        // when
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/v1/users/me/password",
                PUT,
                sendRequestAsUser(newPasswordData),
                Void.class
        );
        // then
        assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
        assertThat(response.getBody()).isNull();
    }
}
