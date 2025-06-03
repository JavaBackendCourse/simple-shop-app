package org.myprojects.simple_shop_app.auth.service.impl;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.myprojects.simple_shop_app.auth.service.JwtService;
import org.myprojects.simple_shop_app.auth.service.TokenManagerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Data
@Service
@Slf4j
public class TokenManagerServiceImpl implements TokenManagerService {
    private final JwtService jwtService;

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${app.settings.cache.ttl.revoked-tokens}")
    private String revokedTokenTtl;

    @Override
    public Boolean isAccessTokenRevoked(String token) {
        Boolean isRevoked = redisTemplate.hasKey(String.format("revoked:%s", token));

        return isRevoked;
    }

    @Override
    public void onRevokedToken(String token) {
        log.info("[TokenManagerServiceImpl][onRevokedToken] стратовал");
        try {
            redisTemplate.opsForValue().set(
                    String.format("revoked:%s", token),
                    true,
                    Duration.ofSeconds(Long.parseLong(revokedTokenTtl))
            );
            log.info("[TokenManagerServiceImpl][onRevokedToken] успешно отработал");
        } catch (Exception e) {
            log.info("[TokenManagerServiceImpl][onRevokedToken] поулчена ошибка: {}", e.getMessage(), e);
            throw e;
        }
    }
}
