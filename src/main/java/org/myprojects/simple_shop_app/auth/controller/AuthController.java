package org.myprojects.simple_shop_app.auth.controller;

import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.Pair;
import org.myprojects.simple_shop_app.auth.model.request.LoginRequest;
import org.myprojects.simple_shop_app.auth.model.request.RefreshTokenRequest;
import org.myprojects.simple_shop_app.auth.model.request.RegistrationRequest;
import org.myprojects.simple_shop_app.auth.model.response.LoginResponse;
import org.myprojects.simple_shop_app.auth.model.response.RefreshTokenResponse;
import org.myprojects.simple_shop_app.auth.model.response.RegistrationResponse;
import org.myprojects.simple_shop_app.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@RequestBody RegistrationRequest request) {
        Pair<String, String> tokens = authService.register(request);

        return ResponseEntity.ok(
                RegistrationResponse.builder()
                        .accessToken(tokens.a)
                        .refreshToken(tokens.b)
                        .build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> register(@RequestBody LoginRequest request) {
        Pair<String, String> tokens = authService.login(request);

        return ResponseEntity.ok(
                LoginResponse.builder()
                        .accessToken(tokens.a)
                        .refreshToken(tokens.b)
                        .build()
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> register(@RequestBody RefreshTokenRequest request) {
        Pair<String, String> tokens = authService.refreshToken(request);

        return ResponseEntity.ok(
                RefreshTokenResponse.builder()
                        .accessToken(tokens.a)
                        .refreshToken(tokens.b)
                        .build()
        );
    }
}
