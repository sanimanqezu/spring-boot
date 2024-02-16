package za.co.example.core.impl;

import com.example.users_service.models.UserDTO;
import org.springframework.stereotype.Service;
import za.co.example.core.service.IUsersService;
import za.co.example.exceptions.UserNotFoundException;
import za.co.example.exceptions.UsersNotFoundException;
import za.co.example.mapers.UserMapper;
import za.co.example.persistance.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsersServiceImpl implements IUsersService {

    private final UserRepository userRepository;

    public UsersServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void addUser(UserDTO userDTO) {
        String rsaId = userDTO.getRsaId();

        if (rsaId.length() != 13) {
            throw new UserNotFoundException("RSA Id must have 13 digits");
        }
        userRepository.save(UserMapper.USER_MAPPER.dtoToEntity(userDTO));
    }

    @Override
    public void removeUser(Long id) {
        boolean user = userRepository.existsById(id);

        if (!user) {
            throw new UserNotFoundException("Id", id);
        }
        userRepository.delete(UserMapper.USER_MAPPER.dtoToEntity(getUserById(id)));
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<UserDTO> users = UserMapper.USER_MAPPER.entityToDto(userRepository.findAll());

        if (users.isEmpty()) {
            throw new UsersNotFoundException("No user was found!!");
        }
        return users;
    }

    @Override
    public UserDTO getUserById(Long id) {
        UserDTO userOptional = UserMapper.USER_MAPPER.entityToDto(userRepository.findById(id).get());

        if (userOptional == null) {
            throw new UserNotFoundException("Id", id);
        }
        return userOptional;
    }

    @Override
    public List<UserDTO> getUsersByFirstName(String firstName) {
        List<UserDTO> users = UserMapper.USER_MAPPER.entityToDto(userRepository.findByFirstName(firstName));
        if (users == null || users.isEmpty()) {
            throw new UsersNotFoundException("First Name", firstName);
        }
        return users;
    }

    @Override
    public List<UserDTO> getUsersByLastName(String lastName) {
        List<UserDTO> users = UserMapper.USER_MAPPER.entityToDto(userRepository.findByLastName(lastName));
        if (users == null || users.isEmpty()) {
            throw new UsersNotFoundException("Last Name", lastName);
        }
        return users;
    }

    @Override
    public UserDTO getUserByRsaId(String rsaId) {
        UserDTO userDTO = UserMapper.USER_MAPPER.entityToDto(userRepository.findByRsaId(rsaId));
        if (userDTO == null) {
            throw new UserNotFoundException("RSA ID", rsaId);
        }
        return userDTO;
    }

    @Override
    public void updateUser(Long id, UserDTO updatedUser) {
        UserDTO existingUser = getUserById(id);
        if (existingUser != null && updatedUser != null) {
            if (updatedUser.getFirstName() != null && !updatedUser.getFirstName().isEmpty()) existingUser.setFirstName(updatedUser.getFirstName());
            if (updatedUser.getLastName() != null && !updatedUser.getLastName().isEmpty()) existingUser.setLastName(updatedUser.getLastName());
            if (updatedUser.getRsaId() != null && !updatedUser.getRsaId().isEmpty()) existingUser.setRsaId(updatedUser.getRsaId());
            userRepository.save(UserMapper.USER_MAPPER.dtoToEntity(existingUser));
        }
    }

    @Override
    public List<UserDTO> searchUsers(Long id, String firstName, String lastName, String rsaId) {
        List<UserDTO> users = new ArrayList<>();
        if (id != null) {
            users.add(UserMapper.USER_MAPPER.entityToDto(userRepository.findById(id).get()));
        }
        if (firstName != null && !firstName.isEmpty()) {
            users.addAll(UserMapper.USER_MAPPER.entityToDto(userRepository.findByFirstName(firstName)));
        }
        if (lastName != null && !lastName.isEmpty()) {
            users.addAll(UserMapper.USER_MAPPER.entityToDto(userRepository.findByLastName(lastName)));
        }
        if (rsaId != null && !rsaId.isEmpty()) {
            users.add(UserMapper.USER_MAPPER.entityToDto(userRepository.findByRsaId(rsaId)));
        }
        return users;
    }
}
