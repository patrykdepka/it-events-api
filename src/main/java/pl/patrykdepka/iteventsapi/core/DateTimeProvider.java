package pl.patrykdepka.iteventsapi.core;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class DateTimeProvider {
    private final Clock clock;

    public LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now(clock);
    }
}
