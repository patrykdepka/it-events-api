package pl.patrykdepka.iteventsapi.core;

import lombok.Getter;

import java.util.Date;

@Getter
public class ErrorResponse {
    private final Date timestamp;
    private final int status;
    private final String error;
    private String message;
    private final String path;

    public ErrorResponse(Date timestamp, int status, String error, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.path = path;
    }

    public ErrorResponse(Date timestamp, int status, String error, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}
