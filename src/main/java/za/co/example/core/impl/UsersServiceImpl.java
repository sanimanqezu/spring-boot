package za.co.example.core.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.example.core.service.IUsersService;
import za.co.example.dto.UserDTO;
import za.co.example.exceptions.UserFoundException;
import za.co.example.exceptions.UserNotFoundException;
import za.co.example.exceptions.UsersNotFoundException;
import za.co.example.mappers.UserMapper;
import za.co.example.persistence.entities.User;
import za.co.example.persistence.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsersServiceImpl implements IUsersService {

    private static final Logger logger = LoggerFactory.getLogger(UsersServiceImpl.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public void addUser(UserDTO userDTO) {
        if (userRepository.existsByRsaId(userDTO.getRsaId())) {
            throw new UserFoundException("rsaId", userDTO.getRsaId());
        }
        userRepository.save(userMapper.dtoToEntity(userDTO));
        logger.debug("User created with RSA ID: {}", userDTO.getRsaId());
    }

    @Override
    @Transactional
    public void removeUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("id", id);
        }
        userRepository.deleteById(id);
        logger.debug("User deleted with id: {}", id);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<UserDTO> users = userMapper.entityToDto(userRepository.findAll());
        if (users.isEmpty()) {
            throw new UsersNotFoundException("No users found");
        }
        return users;
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("id", id));
        return userMapper.entityToDto(user);
    }

    @Override
    public List<UserDTO> getUsersByFirstName(String firstName) {
        List<UserDTO> users = userMapper.entityToDto(userRepository.findByFirstName(firstName));
        if (users.isEmpty()) {
            throw new UsersNotFoundException("firstName", firstName);
        }
        return users;
    }

    @Override
    public List<UserDTO> getUsersByLastName(String lastName) {
        List<UserDTO> users = userMapper.entityToDto(userRepository.findByLastName(lastName));
        if (users.isEmpty()) {
            throw new UsersNotFoundException("lastName", lastName);
        }
        return users;
    }

    @Override
    public UserDTO getUserByRsaId(String rsaId) {
        User user = userRepository.findByRsaId(rsaId)
                .orElseThrow(() -> new UserNotFoundException("rsaId", rsaId));
        return userMapper.entityToDto(user);
    }

    @Override
    @Transactional
    public void updateUser(Long id, UserDTO updatedUser) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("id", id));

        if (updatedUser.getFirstName() != null && !updatedUser.getFirstName().isBlank()) {
            existing.setFirstName(updatedUser.getFirstName());
        }
        if (updatedUser.getLastName() != null && !updatedUser.getLastName().isBlank()) {
            existing.setLastName(updatedUser.getLastName());
        }
        if (updatedUser.getRsaId() != null && !updatedUser.getRsaId().isBlank()) {
            if (!existing.getRsaId().equals(updatedUser.getRsaId())
                    && userRepository.existsByRsaId(updatedUser.getRsaId())) {
                throw new UserFoundException("rsaId", updatedUser.getRsaId());
            }
            existing.setRsaId(updatedUser.getRsaId());
        }
        userRepository.save(existing);
        logger.debug("User updated with id: {}", id);
    }

    @Override
    public List<UserDTO> searchUsers(Long id, String firstName, String lastName, String rsaId) {
        List<User> results = userRepository.search(id, firstName, lastName, rsaId);
        if (results.isEmpty()) {
            throw new UsersNotFoundException("No users matched the search criteria");
        }
        return userMapper.entityToDto(results);
    }
}
