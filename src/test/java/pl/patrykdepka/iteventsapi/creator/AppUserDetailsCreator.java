package pl.patrykdepka.iteventsapi.creator;

import org.springframework.security.core.GrantedAuthority;
import pl.patrykdepka.iteventsapi.appuser.domain.AppUser;
import pl.patrykdepka.iteventsapi.security.AppUserDetails;

import java.util.Base64;

public class AppUserDetailsCreator {

    public static AppUserDetails create(AppUser user) {
        return new AppUserDetails.AppUserDetailsBuilder()
                .profileImageType(user.getProfileImage().getType())
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
}
