package pl.patrykdepka.iteventsapi.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.patrykdepka.iteventsapi.appuser.domain.AppUser;
import pl.patrykdepka.iteventsapi.appuser.domain.AppUserRepository;

import javax.transaction.Transactional;
import java.util.Base64;

@Service
public class AppUserDetailsServiceImpl implements AppUserDetailsService {
    private final AppUserRepository appUserRepository;

    public AppUserDetailsServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository
                .findByEmail(username)
                .map(this::createAppUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with email %s not found", username)));
    }

    private UserDetails createAppUserDetails(AppUser user) {
        return new AppUserDetails.AppUserDetailsBuilder()
                .profileImageType(user.getProfileImage().getFileType())
                .profileImageData(Base64.getEncoder().encodeToString(user.getProfileImage().getFileData()))
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getEmail())
                .password(user.getPassword())
                .enabled(user.isEnabled())
                .accountNonLocked(user.isAccountNonLocked())
                .authorities(user.getRoles()
                        .stream()
                        .map(role -> (GrantedAuthority) role::name)
                        .toList())
                .build();
    }

    public void updateAppUserDetails(AppUser user) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        Authentication newAuthenticationToken = new UsernamePasswordAuthenticationToken(createAppUserDetails(user), currentUser.getCredentials(), currentUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuthenticationToken);
    }
}
