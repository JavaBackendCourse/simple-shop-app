package org.myprojects.simple_shop_app.auth.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.myprojects.simple_shop_app.auth.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.function.Function;

@Data
@Service
@Slf4j
public class JwtServiceImpl implements JwtService {
    @Value("${app.settings.security.jwt.secret-key}")
    private String secretKey;

    @Value("${app.settings.security.jwt.expiration}")
    private String jwtTokenExpiration;

    private DateTimeFormatter defaultDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private Claims extractAllTokenClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(generateSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractTokenClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllTokenClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public String extractUsernameFromToken(String token) {
        return extractTokenClaim(token, Claims::getSubject);
    }

    private Date extractExpirationFromToken(String token) {
        return extractTokenClaim(token, Claims::getExpiration);
    }

    @Override
    public Boolean isTokenValid(String token) {
        return !isTokenExpired(token);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpirationFromToken(token).before(new Date());
    }

    private Key generateSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
