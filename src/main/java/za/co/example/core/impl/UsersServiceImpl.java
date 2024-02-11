package za.co.example.core.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import za.co.example.exceptions.UserFoundException;
import za.co.example.exceptions.UserNotFoundException;
import za.co.example.exceptions.UsersNotFoundException;
import za.co.example.persistance.entities.User;
import za.co.example.persistance.repository.UserRepository;
import za.co.example.core.service.IUsersService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsersServiceImpl implements IUsersService {

    private final UserRepository userRepository;

    public UsersServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void addUser(User user) {
        Long id = user.getId();
        String rsaId = user.getRsaId();

        if (id != null) {
            boolean existingUser = userRepository.existsById(id);
            if (existingUser) {
                throw new UserFoundException("Id", id);
            }
        }

        if (rsaId.length() != 13) {
            throw new UserNotFoundException("RSA Id must have 13 digits");
        }
        userRepository.save(user);
    }

    @Override
    public void removeUser(Long id) {
        boolean user = userRepository.existsById(id);

        if (!user) {
            throw new UserNotFoundException("Id", id);
        }
        userRepository.delete(getUserById(id));
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            throw new UsersNotFoundException("No user was found!!");
        }
        return users;
    }

    @Override
    public User getUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("Id", id);
        }
        return userOptional.get();
    }

    @Override
    public List<User> getUsersByFirstName(String firstName) {
        List<User> users = userRepository.findByFirstName(firstName);
        if (users == null || users.isEmpty()) {
            throw new UsersNotFoundException("First Name", firstName);
        }
        return users;
    }

    @Override
    public List<User> getUsersByLastName(String lastName) {
        List<User> users = userRepository.findByLastName(lastName);
        if (users == null || users.isEmpty()) {
            throw new UsersNotFoundException("Last Name", lastName);
        }
        return users;
    }

    @Override
    public User getUserByRsaId(String rsaId) {
        User user = userRepository.findByRsaId(rsaId);
        if (user == null) {
            throw new UserNotFoundException("RSA ID", rsaId);
        }
        return user;
    }

    @Override
    public void updateUser(Long id, User updatedUser) {
        User existingUser = getUserById(id);
        if (existingUser != null && updatedUser != null) {
            if (updatedUser.getFirstName() != null && !updatedUser.getFirstName().isEmpty()) existingUser.setFirstName(updatedUser.getFirstName());
            if (updatedUser.getLastName() != null && !updatedUser.getLastName().isEmpty()) existingUser.setLastName(updatedUser.getLastName());
            if (updatedUser.getRsaId() != null && !updatedUser.getRsaId().isEmpty()) existingUser.setRsaId(updatedUser.getRsaId());
            userRepository.save(existingUser);
        }
    }

    @Override
    public List<User> searchUsers(Long id, String firstName, String lastName, String rsaId) {
        List<User> users = new ArrayList<>();
        if (id != null) {
            Optional<User> user = userRepository.findById(id);
            user.ifPresent(users::add);
        }
        if (firstName != null && !firstName.isEmpty()) {
            users.addAll(userRepository.findByFirstName(firstName));
        }
        if (lastName != null && !lastName.isEmpty()) {
            users.addAll(userRepository.findByLastName(lastName));
        }
        if (rsaId != null && !rsaId.isEmpty()) {
            users.add(userRepository.findByRsaId(rsaId));
        }
        return users;
    }
}
