package eu.vargapeter.demo.model;

import eu.vargapeter.demo.model.dto.UserDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toUserDTO(User user);

    List<UserDTO> toUserDTOs(List<User> users);

    User toUser(UserDTO userDTO);
}
