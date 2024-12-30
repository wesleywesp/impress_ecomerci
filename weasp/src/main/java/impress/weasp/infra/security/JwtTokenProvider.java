package impress.weasp.infra.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import impress.weasp.infra.exception.JwtTokenException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${api.secutiry.token.secret}")
    private String secretKey;

    @Value("${api.secutiry.token.expiration}")
    private long expirationTime;

    @Value("${api.secutiry.token.header}")
    private String headerString;

    private Algorithm algorithm;
    private JWTVerifier verifier;

    @PostConstruct
    protected void init() {
        try {
            algorithm = Algorithm.HMAC256(secretKey.getBytes());
            verifier = JWT.require(algorithm)
                    .withIssuer(headerString)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao inicializar JWT", e);
        }
    }

    public String generateToken(String email, String role) {
        return JWT.create()
                .withIssuer(headerString)
                .withSubject(email)
                .withClaim("role", role)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(algorithm);
    }

    public String validateToken(String token) {
        try {
            // Remove o prefixo "Bearer " se estiver presente
            if (token.startsWith("Bearer ")) {
                token = token.substring(7).trim();
            }
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getSubject();
        } catch (IllegalArgumentException e) {
            throw new JwtTokenException("Token inválido", e);
        }
    }

    public String getRoleFromToken(String token) {
        try {
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaim("role").asString();
        } catch (IllegalArgumentException e) {
            throw new JwtTokenException("Erro ao obter role do token", e);
        }
    }
}