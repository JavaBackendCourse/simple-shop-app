package org.myprojects.simple_shop_app.user.repository;

import org.myprojects.simple_shop_app.user.model.User;
import org.myprojects.simple_shop_app.user.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByIdAndRole(Long id, UserRole userRole);
}
