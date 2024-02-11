package za.co.example.core.service;

import za.co.example.persistance.entities.User;

import java.util.List;

public interface IUsersService {

    void addUser(User user);

    void removeUser(Long id);

    List<User> getAllUsers();

    User getUserById(Long id);

    List<User> getUsersByFirstName(String firstName);

    List<User> getUsersByLastName(String lastName);

    User getUserByRsaId(String rsaId);

    void updateUser(Long id, User updatedUser);

    List<User> searchUsers(Long id, String firstName, String lastName, String rsaId);
}
