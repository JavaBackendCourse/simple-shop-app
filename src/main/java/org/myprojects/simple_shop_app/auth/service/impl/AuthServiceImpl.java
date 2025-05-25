package org.myprojects.simple_shop_app.auth.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.Pair;
import org.myprojects.simple_shop_app.auth.model.request.LoginRequest;
import org.myprojects.simple_shop_app.auth.model.request.RefreshTokenRequest;
import org.myprojects.simple_shop_app.auth.model.request.RegistrationRequest;
import org.myprojects.simple_shop_app.auth.model.token.RefreshToken;
import org.myprojects.simple_shop_app.auth.model.user.UserDetailsAdapter;
import org.myprojects.simple_shop_app.auth.service.AuthService;
import org.myprojects.simple_shop_app.auth.service.TokenManagerService;
import org.myprojects.simple_shop_app.user.model.User;
import org.myprojects.simple_shop_app.user.model.UserRole;
import org.myprojects.simple_shop_app.user.model.request.CreateUserRequest;
import org.myprojects.simple_shop_app.user.service.UserService;
import org.myprojects.simple_shop_app.utils.converters.JsonConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final String PASSWORD_MASK = "******";

    private final UserService userService;

    private final TokenManagerService tokenManagerService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public Pair<String, String> register(RegistrationRequest registrationRequest) {
        log.info("[AuthServiceImpl][register] стартовал: {}", JsonConverter.toJson(
                registrationRequest.toBuilder().password(PASSWORD_MASK).build()).orElse(""));

        try {
            User createdUser = userService.createUser(
                    CreateUserRequest.builder()
                            .firstName(registrationRequest.firstName())
                            .lastName(registrationRequest.lastName())
                            .middleName(registrationRequest.middleName())
                            .birthDate(registrationRequest.birthDate())
                            .email(registrationRequest.email())
                            .phoneNumber(registrationRequest.phoneNumber())
                            .role(UserRole.valueOf(registrationRequest.role()))
                            .encodedPassword(passwordEncoder.encode(registrationRequest.password()))
                            .build()
            );

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                            registrationRequest.email(),
                            registrationRequest.password()
                    )
            );

            //Generating tokens
            Pair<String, String> tokens = tokenManagerService.generateAccessAndRefreshTokens(
                    createdUser.getId(),
                    UserDetailsAdapter.builder().user(createdUser).build()
            );

            log.info("[AuthServiceImpl][register] успещно отработал");

            return tokens;
        } catch (Exception e) {
            log.error("[AuthServiceImpl][register] получена ошибка: {}", e.getMessage(), e);

            throw new RuntimeException("Ошибка при регистрации нового пользователя!");
        }
    }

    @Override
    @Transactional
    public Pair<String, String> login(LoginRequest loginRequest) {
        log.info("[AuthServiceImpl][login] стартовал: {}", JsonConverter.toJson(
                loginRequest.toBuilder().password(PASSWORD_MASK).build()).orElse(""));

        try {
            Optional<User> userOpt = userService.getUserByEmail(loginRequest.email());

            if (userOpt.isEmpty() || !passwordEncoder.matches(loginRequest.password(), userOpt.get().getPassword()))
                throw new IllegalArgumentException("Некорректный логин или пароль!");

            User user = userOpt.get();

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                            loginRequest.email(),
                            loginRequest.password()
                    )
            );

            Pair<String, String> tokens = tokenManagerService.generateAccessAndRefreshTokens(
                    user.getId(),
                    UserDetailsAdapter.builder().user(user).build()
            );

            log.info("[AuthServiceImpl][login] успещно отработал");

            return tokens;
        } catch (IllegalArgumentException e) {
            log.error("[AuthServiceImpl][login] получена ошибка IllegalArgumentException: {}", e.getMessage(), e);

            throw e;
        } catch (Exception e) {
            log.error("[AuthServiceImpl][login] получена ошибка: {}", e.getMessage(), e);

            throw new RuntimeException("Ошибка при входе пользователя в систему!");
        }
    }

    @Override
    public Pair<String, String> refreshToken(RefreshTokenRequest refreshTokenRequest) {
        log.info("[AuthServiceImpl][refreshToken] стратовал");

        try {
            RefreshToken refreshToken = tokenManagerService.getRefreshTokenByToken(refreshTokenRequest.refreshToken())
                    .orElseThrow(() -> new IllegalArgumentException("Невалидный рефреш токен!"));

            tokenManagerService.verifyRefreshToken(refreshToken);

            Optional<User> userOpt = userService.getUserById(refreshToken.getUserId());
            User user = userOpt.orElseThrow(() -> new RuntimeException("Пользователь не найден!"));

            Pair<String, String> tokens = tokenManagerService.generateAccessAndRefreshTokens(
                    user.getId(),
                    UserDetailsAdapter.builder().user(user).build()
            );

            log.info("[AuthServiceImpl][refreshToken] успешно отработал");

            return tokens;
        } catch (IllegalArgumentException e) {
            log.error("[AuthServiceImpl][refreshToken] получена ошибка IllegalArgumentException: {}", e.getMessage(), e);

            throw e;
        } catch (Exception e) {
            log.error("[AuthServiceImpl][refreshToken] получена ошибка: {}", e.getMessage(), e);

            throw new RuntimeException("Ошибка при рефреше токена пользователя!");
        }
    }
}
