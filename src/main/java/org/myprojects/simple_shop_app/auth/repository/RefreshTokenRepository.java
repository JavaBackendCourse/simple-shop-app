package org.myprojects.simple_shop_app.auth.repository;

import jakarta.transaction.Transactional;
import org.myprojects.simple_shop_app.auth.model.token.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String refreshToken);

    @Transactional
    void deleteAllByUserId(Long userId);
}
