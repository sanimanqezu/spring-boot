package za.co.example.core.service;

import com.example.users_service.models.UserDTO;
import java.util.List;

public interface IUsersService {

    void addUser(UserDTO userDTO);

    void removeUser(Long id);

    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long id);

    List<UserDTO> getUsersByFirstName(String firstName);

    List<UserDTO> getUsersByLastName(String lastName);

    UserDTO getUserByRsaId(String rsaId);

    void updateUser(Long id, UserDTO updatedUser);

    List<UserDTO> searchUsers(Long id, String firstName, String lastName, String rsaId);
}
