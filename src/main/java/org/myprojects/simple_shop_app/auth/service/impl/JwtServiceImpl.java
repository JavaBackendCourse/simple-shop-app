package org.myprojects.simple_shop_app.auth.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.myprojects.simple_shop_app.auth.model.user.UserDetailsAdapter;
import org.myprojects.simple_shop_app.auth.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Data
@Service
@Slf4j
public class JwtServiceImpl implements JwtService {
    @Value("${app.settings.security.jwt.secret-key}")
    private String secretKey;

    @Value("${app.settings.security.jwt.expiration}")
    private String jwtTokenExpiration;

    public String generateJwtToken(
            UserDetails userDetails
    ) {
        return buildToken(getExtraClaims(userDetails), userDetails, Long.parseLong(jwtTokenExpiration));
    }

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

    @Override
    public Long extractUserIdFromToken(String token) {
        return extractTokenClaim(token, claims -> claims.get("id", Long.class));
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

    private Map<String, Object> getExtraClaims(UserDetails userDetails) {
        HashMap<String, Object> extraClaims = new HashMap<>();

        if (userDetails instanceof UserDetailsAdapter userDetailsAdapter) {
            extraClaims.put("id", userDetailsAdapter.getUser().getId());
            extraClaims.put("firstName", userDetailsAdapter.getUser().getFirstName());
            extraClaims.put("lastName", userDetailsAdapter.getUser().getLastName());
            extraClaims.put("middleName", userDetailsAdapter.getUser().getMiddleName());
            extraClaims.put("birthDate", userDetailsAdapter.getUser().getBirthDate());
            extraClaims.put("email", userDetailsAdapter.getUser().getEmail());
            extraClaims.put("phoneNumber", userDetailsAdapter.getUser().getPhoneNumber());
            extraClaims.put("role", userDetailsAdapter.getUser().getRole().toString());
            extraClaims.put("status", userDetailsAdapter.getUser().getStatus().getValue());
        }

        return extraClaims;
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(generateSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key generateSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
