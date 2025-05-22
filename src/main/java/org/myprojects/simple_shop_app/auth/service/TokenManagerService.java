package org.myprojects.simple_shop_app.auth.service;

import org.antlr.v4.runtime.misc.Pair;
import org.myprojects.simple_shop_app.auth.model.token.RefreshToken;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface TokenManagerService {
    Pair<String, String> generateAccessAndRefreshTokens(Long userId, UserDetails userDetails) throws Exception;

    void deleteAllTokensByUserId(Long userId);

    void verifyRefreshToken(RefreshToken refreshToken);

    Optional<RefreshToken> getRefreshTokenByToken(String token);

    Long getUserIdFromAccessToken(String token);

    Boolean doesAccessTokenExist(String token);
}
