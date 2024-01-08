package pl.patrykdepka.iteventsapi.appuser.facade;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.patrykdepka.iteventsapi.appuser.domain.AppUser;
import pl.patrykdepka.iteventsapi.appuser.domain.AppUserRepository;
import pl.patrykdepka.iteventsapi.appuser.domain.CurrentUserFacade;
import pl.patrykdepka.iteventsapi.appuser.domain.CurrentUserFacadeImpl;
import pl.patrykdepka.iteventsapi.creator.AppUserCreator;
import pl.patrykdepka.iteventsapi.creator.AppUserDetailsCreator;
import pl.patrykdepka.iteventsapi.security.AppUserDetails;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class CurrentUserFacadeImplUnitTest {
    private AppUserRepository appUserRepository;
    private CurrentUserFacade currentUserFacade;

    @BeforeEach
    void setUp() {
        appUserRepository = Mockito.mock(AppUserRepository.class);
        currentUserFacade = new CurrentUserFacadeImpl(appUserRepository);
    }

    @AfterEach
    public void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldReturnUserIfIsAuthenticated() {
        // given
        AppUser user = AppUserCreator.create(2L, "Jan", "Kowalski");
        AppUserDetails userDetails = AppUserDetailsCreator.create(user);
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, "tests", userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        when(appUserRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        // when
        AppUser currentUser = currentUserFacade.getCurrentUser();
        // then
        assertNotNull(currentUser);
    }

    @Test
    void shouldThrowExceptionIfUserDoesNotExist() {
        // given
        AppUser user = AppUserCreator.create(2L, "Jan", "Kowalski");
        AppUserDetails userDetails = AppUserDetailsCreator.create(user);
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, "tests", userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        // then
        assertThatThrownBy(() -> currentUserFacade.getCurrentUser())
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage(String.format("User with name %s not found", user.getEmail()));
    }
}
