package pl.patrykdepka.iteventsapp.appuser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.patrykdepka.iteventsapp.appuser.model.AppUser;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByEmail(String username);
}
