package com.juliano.meufin.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.juliano.meufin.domain.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.*;


@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;
    @Value("${api.security.token.issuer}")
    private String issuer;
    public String generate(User user) {
        var algorithm = Algorithm.HMAC256(secret);
        try {
            return JWT.create()
                    .withExpiresAt(expiresAt())
                    .withIssuer(issuer)
                    .withSubject(String.valueOf(user.getId()))
                    .sign(algorithm);
        } catch (JWTCreationException ex) {
            return "it was not possible to create a new JWTToken";
        }

    }

    public String validateToken(String token)  {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException ex) {
            return ex.getMessage();
        }
    }

    private Instant expiresAt() {
        return LocalDateTime.now().plusDays(10).toInstant(ZoneOffset.of("-03:00"));
    }
}
