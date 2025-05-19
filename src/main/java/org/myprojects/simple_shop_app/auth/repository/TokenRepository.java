package org.myprojects.simple_shop_app.auth.repository;

import jakarta.transaction.Transactional;
import org.myprojects.simple_shop_app.auth.model.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);

    @Transactional
    void deleteAllByUserId(Long userId);
}
