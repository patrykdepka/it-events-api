package pl.patrykdepka.iteventsapi.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtils {
    public static final String TOKEN_PREFIX = "Bearer ";
    private final String secret;

    public JwtUtils(@Value("${application.security.jwt.secret}") String secret) {
        this.secret = secret;
    }

    public String generateToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1L)))
                .sign(Algorithm.HMAC256(secret));
    }

    public boolean validateToken(String token) {
        JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
        return true;
    }

    public String extractUsername(String token) {
        return JWT.decode(token).getSubject();
    }
}
