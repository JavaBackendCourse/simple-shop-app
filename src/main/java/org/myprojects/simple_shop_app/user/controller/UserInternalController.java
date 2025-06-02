package org.myprojects.simple_shop_app.user.controller;

import lombok.RequiredArgsConstructor;
import org.myprojects.simple_shop_app.user.mapper.UserMapper;
import org.myprojects.simple_shop_app.user.model.User;
import org.myprojects.simple_shop_app.user.model.dto.UserDTO;
import org.myprojects.simple_shop_app.user.model.request.CreateUserRequest;
import org.myprojects.simple_shop_app.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
public class UserInternalController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody CreateUserRequest request) {
        User createdUser = userService.createUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED.value())
                .body(UserMapper.INSTANCE.userToUserDTO(createdUser));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.getUserByEmail(email);

        return user.map(UserMapper.INSTANCE::userToUserDTO).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);

        return user.map(UserMapper.INSTANCE::userToUserDTO).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


}
