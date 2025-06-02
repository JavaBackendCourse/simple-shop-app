package org.myprojects.simple_shop_app.auth.service.impl;

import lombok.Data;
import org.myprojects.simple_shop_app.auth.service.JwtService;
import org.myprojects.simple_shop_app.auth.service.TokenManagerService;
import org.springframework.stereotype.Service;

@Data
@Service
public class TokenManagerServiceImpl implements TokenManagerService {
    private final JwtService jwtService;

    @Override
    public Boolean doesAccessTokenExist(String token) {
        return true; // TODO add checking revoked tokens from Redis
    }
}
