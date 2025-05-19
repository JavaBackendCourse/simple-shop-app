package org.myprojects.simple_shop_app.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myprojects.simple_shop_app.exception.ItemNotFoundException;
import org.myprojects.simple_shop_app.user.model.User;
import org.myprojects.simple_shop_app.user.model.request.CreateUserRequest;
import org.myprojects.simple_shop_app.user.model.request.UpdateUserRequest;
import org.myprojects.simple_shop_app.user.repository.UserRepository;
import org.myprojects.simple_shop_app.user.service.UserService;
import org.myprojects.simple_shop_app.utils.converters.JsonConverter;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User createUser(CreateUserRequest createUserRequest) {
        try {
            log.info("[UserServiceImpl][createUser] стартовал: {}", JsonConverter.toJson(createUserRequest).orElse(""));
            var response = userRepository.save(
                    User.builder()
                            .firstName(createUserRequest.firstName())
                            .lastName(createUserRequest.lastName())
                            .middleName(createUserRequest.middleName())
                            .birthDate(createUserRequest.birthDate())
                            .phoneNumber(createUserRequest.phoneNumber())
                            .email(createUserRequest.email())
                            .password(createUserRequest.encodedPassword())
                            .role(createUserRequest.role())
                            .build()
            );

            log.info("[UserServiceImpl][createUser] успешно отработал: {}", JsonConverter.toJson(response).orElse(""));

            return response;
        } catch (Exception e) {
            log.error("[UserServiceImpl][createUser] получена ошибка: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при регистрации нового пользователя");
        }
    }

    @Override
    public User updateUser(UpdateUserRequest updateUserRequest) {
        try {
            log.info("[UserServiceImpl][updateUser] стартовал: {}", JsonConverter.toJson(updateUserRequest).orElse(""));
            var user = userRepository.findById(updateUserRequest.userId())
                    .orElseThrow(
                            () -> new ItemNotFoundException("Пользователь не найден")
                    );

            updateUserRequest.firstName().ifPresent(user::setFirstName);
            updateUserRequest.lastName().ifPresent(user::setLastName);
            updateUserRequest.middleName().ifPresent(user::setMiddleName);
            updateUserRequest.birthDate().ifPresent(user::setBirthDate);
            updateUserRequest.role().ifPresent(user::setRole);
            updateUserRequest.email().ifPresent(user::setEmail);
            updateUserRequest.phoneNumber().ifPresent(user::setPhoneNumber);
            updateUserRequest.status().ifPresent(user::setStatus);
            updateUserRequest.newEncodedPassword().ifPresent(user::setPassword);

            var response = userRepository.save(user);

            log.info("[UserServiceImpl][updateUser] получен успешный ответ: {}",
                    JsonConverter.toJson(response).orElse(""));

            return response;
        } catch (Exception e) {
            log.error("[UserServiceImpl][updateUser] получена ошибка: {}", e.getMessage(), e);

            throw new RuntimeException("Ошибка при обновлении пользователя");
        }
    }

    @Override
    public void deleteUser(Long userId) {

    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        try {
            log.info("[UserServiceImpl][getUserByEmail][email={}] стартовал", email);
            var response = userRepository.findByEmail(email);

            log.info("[UserServiceImpl][getUserByEmail][email={}] получен успешный ответ: {}",
                    email, JsonConverter.toJson(response).orElse(""));
            return response;
        } catch (Exception e) {
            log.error("[UserServiceImpl][getUserByEmail][email={}]  получена ошибка: {}", email, e.getMessage(), e);

            throw new RuntimeException("Ошибка при получении пользователя по почте");
        }
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        try {
            log.info("[UserServiceImpl][getUserById][userId={}] стартовал", userId);
            var response = userRepository.findById(userId);

            log.info("[UserServiceImpl][getUserById][userId={}] получен успешный ответ: {}",
                    userId, JsonConverter.toJson(response).orElse(""));
            return response;
        } catch (Exception e) {
            log.error("[UserServiceImpl][getUserById][userId={}]  получена ошибка: {}", userId, e.getMessage(), e);

            throw new RuntimeException("Ошибка при получении пользователя по id");
        }
    }
}
