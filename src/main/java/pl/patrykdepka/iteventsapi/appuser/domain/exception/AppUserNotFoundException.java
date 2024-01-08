package pl.patrykdepka.iteventsapi.appuser.domain.exception;

public class AppUserNotFoundException extends RuntimeException {

    public AppUserNotFoundException(String message) {
        super(message);
    }
}
