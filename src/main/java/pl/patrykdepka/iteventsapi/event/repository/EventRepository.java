package pl.patrykdepka.iteventsapi.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import pl.patrykdepka.iteventsapi.appuser.model.AppUser;
import pl.patrykdepka.iteventsapi.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends PagingAndSortingRepository<Event, Long> {

    List<Event> findFirst10EventsByOrderByDateTimeAsc();

    @Query("SELECT DISTINCT e.city FROM Event e")
    List<String> findAllCities();

    @Query("SELECT e FROM Event e WHERE e.dateTime > :currentDateTime")
    Page<Event> findAllUpcomingEvents(@Param("currentDateTime") LocalDateTime currentDateTime, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.city = :city AND e.dateTime > :currentDateTime")
    Page<Event> findUpcomingEventsByCity(@Param("city") String city, @Param("currentDateTime") LocalDateTime currentDateTime, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.dateTime < :currentDateTime")
    Page<Event> findAllPastEvents(@Param("currentDateTime") LocalDateTime currentDateTime, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.city = :city AND e.dateTime < :currentDateTime")
    Page<Event> findPastEventsByCity(@Param("city") String city, @Param("currentDateTime") LocalDateTime currentDateTime, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.organizer = :organizer")
    Page<Event> findOrganizerEvents(@Param("organizer") AppUser organizer, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.organizer = :organizer AND e.city = :city")
    Page<Event> findOrganizerEventsByCity(@Param("organizer") AppUser organizer, @Param("city") String city, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE :user MEMBER OF e.participants ORDER BY e.dateTime ASC")
    Page<Event> findUserEvents(@Param("user") AppUser user, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE :user MEMBER OF e.participants AND e.city = :city ORDER BY e.dateTime ASC")
    Page<Event> findUserEventsByCity(@Param("user") AppUser user, @Param("city") String city, Pageable pageable);
}
