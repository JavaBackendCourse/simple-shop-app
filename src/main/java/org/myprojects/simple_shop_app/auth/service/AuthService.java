package org.myprojects.simple_shop_app.auth.service;

import org.antlr.v4.runtime.misc.Pair;
import org.myprojects.simple_shop_app.auth.model.request.LoginRequest;
import org.myprojects.simple_shop_app.auth.model.request.RefreshTokenRequest;
import org.myprojects.simple_shop_app.auth.model.request.RegistrationRequest;

public interface AuthService {
    Pair<String, String> register(RegistrationRequest registrationRequest);

    Pair<String, String> login(LoginRequest loginRequest);

    Pair<String, String> refreshToken(RefreshTokenRequest refreshTokenRequest);
}
