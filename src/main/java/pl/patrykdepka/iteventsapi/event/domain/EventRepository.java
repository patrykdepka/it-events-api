package pl.patrykdepka.iteventsapi.event.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import pl.patrykdepka.iteventsapi.appuser.domain.AppUser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends PagingAndSortingRepository<Event, Long> {

    @Query("SELECT e FROM Event e WHERE e.dateTime > :currentDateTime ORDER BY e.dateTime ASC")
    Page<Event> findFirst10UpcomingEvents(LocalDateTime currentDateTime, Pageable pageable);

    @Override
    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.eventImage LEFT JOIN FETCH e.organizer WHERE e.id = :id")
    Optional<Event> findById(Long id);

    @Query("SELECT DISTINCT e.city FROM Event e")
    List<String> findAllCities();

    @Query("SELECT e FROM Event e WHERE e.dateTime > :currentDateTime")
    Page<Event> findUpcomingEvents(LocalDateTime currentDateTime, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.dateTime > :currentDateTime AND e.city = :city")
    Page<Event> findUpcomingEventsByCity(LocalDateTime currentDateTime, String city, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.dateTime < :currentDateTime")
    Page<Event> findPastEvents(LocalDateTime currentDateTime, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.dateTime < :currentDateTime AND e.city = :city")
    Page<Event> findPastEventsByCity(LocalDateTime currentDateTime, String city, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.organizer = :user")
    Page<Event> findOrganizerEvents(AppUser user, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.organizer = :user AND e.city = :city")
    Page<Event> findOrganizerEventsByCity(AppUser user, String city, Pageable pageable);

    @Query("SELECT DISTINCT e FROM Event e LEFT JOIN FETCH e.organizer LEFT JOIN FETCH e.participants WHERE e.id = :id")
    Optional<Event> findEventAndItsParticipantsByEventId(Long id);

    @Query("SELECT DISTINCT e FROM Event e LEFT JOIN FETCH e.participants")
    List<Event> findEventsAndItsParticipants();

    @Query("SELECT DISTINCT e FROM Event e LEFT JOIN FETCH e.participants WHERE e.city = :city")
    List<Event> findEventsAndItsParticipantsByCity(String city);
}
