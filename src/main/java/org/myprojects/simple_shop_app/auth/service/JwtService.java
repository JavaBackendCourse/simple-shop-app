package org.myprojects.simple_shop_app.auth.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateJwtToken(
            UserDetails userDetails
    );

    String extractUsernameFromToken(String token);

    Long extractUserIdFromToken(String token);

    Boolean isTokenValid(String token);
}
