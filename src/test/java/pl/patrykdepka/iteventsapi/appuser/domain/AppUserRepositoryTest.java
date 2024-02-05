package pl.patrykdepka.iteventsapi.appuser.domain;

import org.junit.jupiter.api.Test;
import pl.patrykdepka.iteventsapi.core.BaseIT;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class AppUserRepositoryTest extends BaseIT {

    @Test
    void shouldReturnOptionalOfUserWithGivenIdWhenExists() {
        // given
        var id = 5L;
        // when
        Optional<AppUser> user = appUserRepository.findById(id);
        // then
        assertThat(user.isPresent()).isTrue();
        assertThat(user.get().getId()).isEqualTo(id);
        assertThat(user.get().getProfileImage()).isNotNull();
        assertThat(user.get().getRoles()).isNotEmpty();
    }

    @Test
    void shouldReturnEmptyOptionalWhenUserWithGivenIdDoesNotExists() {
        // given
        var id = 0L;
        // when
        Optional<AppUser> user = appUserRepository.findById(id);
        // then
        assertThat(user.isEmpty()).isTrue();
    }

    @Test
    void shouldReturnOptionalOfUserWithGivenEmailWhenExists() {
        // given
        var email = "patrykkowalski@example.com";
        // when
        Optional<AppUser> user = appUserRepository.findByEmail(email);
        // then
        assertThat(user.isPresent()).isTrue();
        assertThat(user.get().getEmail()).isEqualTo(email);
    }

    @Test
    void shouldReturnEmptyOptionalWhenUserWithGivenEmailDoesNotExists() {
        // given
        var email = "patrykkowalski@example.pl";
        // when
        Optional<AppUser> user = appUserRepository.findByEmail(email);
        // then
        assertThat(user.isEmpty()).isTrue();
    }

    @Test
    void shouldReturnTrueWhenUserWithGivenEmailExists() {
        // given
        var email = "patrykkowalski@example.com";
        // when
        boolean isExists = appUserRepository.existsByEmail(email);
        // then
        assertThat(isExists).isTrue();
    }

    @Test
    void shouldReturnFalseWhenUserWithGivenEmailDoesNotExists() {
        // given
        var email = "patrykkowalski@example.pl";
        // when
        boolean isExists = appUserRepository.existsByEmail(email);
        // then
        assertThat(isExists).isFalse();
    }
}
