package pl.patrykdepka.iteventsapi.appuser.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long>, JpaSpecificationExecutor<AppUser> {

    @Override
    @Query("SELECT au FROM AppUser au LEFT JOIN FETCH au.profileImage LEFT JOIN FETCH au.roles WHERE au.id = :id")
    Optional<AppUser> findById(Long id);

    @Query("SELECT au FROM AppUser au LEFT JOIN FETCH au.profileImage LEFT JOIN FETCH au.roles WHERE au.email = :email")
    Optional<AppUser> findByEmail(String email);

    boolean existsByEmail(String email);
}
