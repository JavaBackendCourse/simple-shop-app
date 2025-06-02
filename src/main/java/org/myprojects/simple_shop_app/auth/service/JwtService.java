package org.myprojects.simple_shop_app.auth.service;

public interface JwtService {
    String extractUsernameFromToken(String token);

    Boolean isTokenValid(String token);
}
