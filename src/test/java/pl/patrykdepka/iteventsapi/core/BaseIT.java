package pl.patrykdepka.iteventsapi.core;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import pl.patrykdepka.iteventsapi.appuser.domain.AppUserRepository;
import pl.patrykdepka.iteventsapi.event.domain.EventRepository;
import pl.patrykdepka.iteventsapi.image.domain.ImageRepository;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.mockito.BDDMockito.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Sql(scripts = "classpath:db/init_db.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:db/clean_db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class BaseIT {
    protected static final int YEAR = LocalDate.now().getYear();
    protected static final Clock fixedDateTime = Clock.fixed(Instant.parse(YEAR + "-06-01T18:00:00.000Z"), ZoneId.of("UTC"));
    protected static final LocalDateTime NOW = LocalDateTime.now(fixedDateTime);
    protected static final LocalDate TODAY = NOW.toLocalDate();

    @Autowired
    protected AppUserRepository appUserRepository;

    @Autowired
    protected ImageRepository imageRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected EventRepository eventRepository;

    @MockBean
    protected Clock clock;

    @BeforeEach
    void setUpClock() {
        given(clock.instant()).willReturn(fixedDateTime.instant());
        given(clock.getZone()).willReturn(fixedDateTime.getZone());
    }
}
