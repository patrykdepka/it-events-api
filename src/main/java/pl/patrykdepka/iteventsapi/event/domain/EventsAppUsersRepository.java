package pl.patrykdepka.iteventsapi.event.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventsAppUsersRepository extends JpaRepository<EventsAppUsers, EventsAppUsersId> {

    boolean existsByEventAndAppUserId(Event event, Long appUserId);
}
