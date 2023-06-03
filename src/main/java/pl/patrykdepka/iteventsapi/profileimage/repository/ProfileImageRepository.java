package pl.patrykdepka.iteventsapi.profileimage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.patrykdepka.iteventsapi.profileimage.model.ProfileImage;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
}
