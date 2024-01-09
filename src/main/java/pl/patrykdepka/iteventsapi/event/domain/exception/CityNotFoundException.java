package pl.patrykdepka.iteventsapi.event.domain.exception;

public class CityNotFoundException extends RuntimeException {

    public CityNotFoundException(String message) {
        super(message);
    }
}
