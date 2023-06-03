package pl.patrykdepka.iteventsapi.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import pl.patrykdepka.iteventsapi.appuser.model.AppUser;

public interface AppUserDetailsService extends UserDetailsService {

    void updateAppUserDetails(AppUser user);
}
