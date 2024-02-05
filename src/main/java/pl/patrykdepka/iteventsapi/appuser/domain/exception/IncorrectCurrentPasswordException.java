package pl.patrykdepka.iteventsapi.appuser.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class IncorrectCurrentPasswordException extends RuntimeException {
    private final String fieldName;
}
