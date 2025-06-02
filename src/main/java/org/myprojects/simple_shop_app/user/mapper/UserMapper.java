package org.myprojects.simple_shop_app.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.myprojects.simple_shop_app.user.model.User;
import org.myprojects.simple_shop_app.user.model.dto.UserDTO;

import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO userToUserDTO(User user);

    User dtoToUser(UserDTO userDTO);

    List<UserDTO> usersToUserDTOs(List<User> users);
}
