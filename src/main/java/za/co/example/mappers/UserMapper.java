package za.co.example.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import za.co.example.dto.UserDTO;
import za.co.example.persistence.entities.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO entityToDto(User user);

    @Mapping(target = "version", ignore = true)
    User dtoToEntity(UserDTO userDTO);

    List<UserDTO> entityToDto(List<User> users);
}
