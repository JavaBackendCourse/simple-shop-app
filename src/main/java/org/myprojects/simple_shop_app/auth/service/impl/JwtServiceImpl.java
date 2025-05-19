package org.myprojects.simple_shop_app.auth.service.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.myprojects.simple_shop_app.auth.model.user.UserDetailsAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@Service
@Slf4j
public class JwtServiceImpl {
    @Value("${app.settings.security.jwt.secret-key}")
    private String secretKey;

    @Value("${app.settings.security.jwt.expiration}")
    private Long jwtTokenExpiration;

    public String generateJwtToken(
            UserDetails userDetails
    ) {
        return buildToken(getExtraClaims(userDetails), userDetails, jwtTokenExpiration);
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
