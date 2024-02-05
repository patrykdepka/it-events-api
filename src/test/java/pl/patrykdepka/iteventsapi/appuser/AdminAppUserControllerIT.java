package pl.patrykdepka.iteventsapi.appuser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.AdminAppUserAccountEditDTO;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.AdminAppUserPasswordEditDTO;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.AdminDeleteAppUserDTO;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.AppUserProfileDTO;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.AppUserProfileEditDTO;
import pl.patrykdepka.iteventsapi.core.BaseControllerIT;
import pl.patrykdepka.iteventsapi.core.TestPageImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;
import static pl.patrykdepka.iteventsapi.appuser.domain.Role.ROLE_ORGANIZER;
import static pl.patrykdepka.iteventsapi.appuser.domain.Role.ROLE_USER;
import static pl.patrykdepka.iteventsapi.creator.AppUserProfileEditDTOCreator.createNewProfileData;

class AdminAppUserControllerIT extends BaseControllerIT {

    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    void shouldReturnUsers(int pageNumber) {
        // when
        ResponseEntity<TestPageImpl<AppUserProfileDTO>> response = restTemplate.exchange(
                "/api/v1/admin/users?page=" + pageNumber,
                GET,
                sendRequestAsAdmin(null),
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
                fromUriString("/api/v1/admin/users/results").queryParam("search_query", "kowalski").toUriString(),
                GET,
                sendRequestAsAdmin(null),
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
                fromUriString("/api/v1/admin/users/results").queryParam("search_query", "patryk+kowalski").toUriString(),
                GET,
                sendRequestAsAdmin(null),
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
    void shouldReturnAccountDataForUserWithGivenId() {
        // given
        var id = 5L;
        // when
        ResponseEntity<AdminAppUserAccountEditDTO> response = restTemplate.exchange(
                "/api/v1/admin/users/" + id + "/account",
                GET,
                sendRequestAsAdmin(null),
                AdminAppUserAccountEditDTO.class
        );
        // then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isEnabled()).isTrue();
        assertThat(response.getBody().isAccountNonLocked()).isTrue();
        assertThat(response.getBody().getRoles().size()).isEqualTo(1);
        assertThat(response.getBody().getRoles()).isEqualTo(List.of(ROLE_USER));
    }

    @Test
    void shouldUpdateAndReturnUpdatedAccountDataForUserWithGivenId() {
        // given
        var id = 5L;
        var newAccountData = new AdminAppUserAccountEditDTO(true, false, List.of(ROLE_ORGANIZER));
        // when
        var response = restTemplate.exchange(
                "/api/v1/admin/users/" + id + "/account",
                PUT,
                sendRequestAsAdmin(newAccountData),
                AdminAppUserAccountEditDTO.class
        );
        // then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isEnabled()).isTrue();
        assertThat(response.getBody().isAccountNonLocked()).isFalse();
        assertThat(response.getBody().getRoles().size()).isEqualTo(1);
        assertThat(response.getBody().getRoles()).isEqualTo(List.of(ROLE_ORGANIZER));
    }

    @Test
    void shouldReturnProfileDataForUserWithGivenId() {
        // given
        var id = 5L;
        // when
        ResponseEntity<AppUserProfileEditDTO> response = restTemplate.exchange(
                "/api/v1/admin/users/" + id + "/profile",
                GET,
                sendRequestAsAdmin(null),
                AppUserProfileEditDTO.class
        );
        // then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCurrentProfileImageData()).isNotBlank();
        assertThat(response.getBody().getProfileImage()).isNull();
        assertThat(response.getBody().getFirstName()).isEqualTo("Patryk");
        assertThat(response.getBody().getLastName()).isEqualTo("Kowalski");
        assertThat(response.getBody().getDateOfBirth()).isEqualTo("1995-10-06");
        assertThat(response.getBody().getCity()).isEqualTo("Rzeszów");
        assertThat(response.getBody().getBio()).isEqualTo("Cześć! Nazywam się Patryk Kowalski i mieszkam w Rzeszowie. Jestem absolwentem informatyki na WSIiZ.");
    }

    @Test
    void shouldUpdateAndReturnUpdatedProfileDataForUserWithGivenId() {
        // given
        var id = 5L;
        var newProfileData = createNewProfileData(
                null, "Testowy", "Użytkownik", "2000-01-01", "Testy Wielkie", "Testowy biogram"
        );
        // when
        ResponseEntity<AppUserProfileEditDTO> response = restTemplate.exchange(
                "/api/v1/admin/users/" + id + "/profile",
                PUT,
                sendRequestAsAdmin(newProfileData),
                AppUserProfileEditDTO.class
        );
        // then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCurrentProfileImageData()).isNotBlank();
        assertThat(response.getBody().getProfileImage()).isNull();
        assertThat(response.getBody().getFirstName()).isEqualTo(newProfileData.getFirstName());
        assertThat(response.getBody().getLastName()).isEqualTo(newProfileData.getLastName());
        assertThat(response.getBody().getDateOfBirth()).isEqualTo(newProfileData.getDateOfBirth());
        assertThat(response.getBody().getCity()).isEqualTo(newProfileData.getCity());
        assertThat(response.getBody().getBio()).isEqualTo(newProfileData.getBio());
    }

    @Test
    void shouldUpdatePasswordForUserWithGivenId() {
        // given
        var id = 5L;
        var newPasswordData = new AdminAppUserPasswordEditDTO("qwerty", "ytrewq", "ytrewq");
        // when
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/v1/admin/users/" + id + "/password",
                PUT,
                sendRequestAsAdmin(newPasswordData),
                Void.class
        );
        // then
        assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
    }

    @Test
    void shouldDeleteUserWithGivenId() {
        // given
        var id = 5L;
        var deleteUserData = new AdminDeleteAppUserDTO("qwerty");
        // when
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/v1/admin/users/" + id,
                DELETE,
                sendRequestAsAdmin(deleteUserData),
                Void.class
        );
        // then
        assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
        assertThat(appUserRepository.findById(id)).isEmpty();
    }
}
