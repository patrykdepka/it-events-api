package pl.patrykdepka.iteventsapi.security;

import lombok.Value;

@Value
public class LoginData {
    String username;
    String password;
}
