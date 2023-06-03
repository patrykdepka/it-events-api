package pl.patrykdepka.iteventsapi.eventimage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.patrykdepka.iteventsapi.eventimage.model.EventImage;

public interface EventImageRepository extends JpaRepository<EventImage, Long> {
}
