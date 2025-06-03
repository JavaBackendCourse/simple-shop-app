package org.myprojects.simple_shop_app.auth.service;

public interface TokenManagerService {
    Boolean isAccessTokenRevoked(String token);

    void onRevokedToken(String token);
}
