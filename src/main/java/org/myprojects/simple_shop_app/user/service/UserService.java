package org.myprojects.simple_shop_app.user.service;

import org.myprojects.simple_shop_app.user.model.User;
import org.myprojects.simple_shop_app.user.model.request.CreateUserRequest;
import org.myprojects.simple_shop_app.user.model.request.UpdateUserRequest;

import java.util.Optional;

public interface UserService {
    User createUser(CreateUserRequest createUserRequest);

    User updateUser(UpdateUserRequest updateUserRequest);

    void deleteUser(Long userId);

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserById(Long userId);
}
