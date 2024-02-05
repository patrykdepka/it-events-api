package pl.patrykdepka.iteventsapi.appuser.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserFacade {

    private final AppUserRepository appUserRepository;

    public AppUser getCurrentUser() {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        return appUserRepository.findByEmail(currentUser.getName())
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with name %s not found", currentUser.getName())));
    }
}
