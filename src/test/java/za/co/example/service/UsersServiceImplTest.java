package za.co.example.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.example.core.impl.UsersServiceImpl;
import za.co.example.dto.UserDTO;
import za.co.example.exceptions.UserFoundException;
import za.co.example.exceptions.UserNotFoundException;
import za.co.example.exceptions.UsersNotFoundException;
import za.co.example.mappers.UserMapper;
import za.co.example.persistence.entities.User;
import za.co.example.persistence.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsersServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UsersServiceImpl usersService;

    // ─── addUser ────────────────────────────────────────────────────────────────

    @Test
    void addUser_savesUser_whenRsaIdIsUnique() {
        UserDTO dto = dto(null, "John", "Doe", "0001234567890");
        User entity = entity(null, "John", "Doe", "0001234567890");

        when(userRepository.existsByRsaId("0001234567890")).thenReturn(false);
        when(userMapper.dtoToEntity(dto)).thenReturn(entity);

        usersService.addUser(dto);

        verify(userRepository).save(entity);
    }

    @Test
    void addUser_throwsUserFoundException_whenRsaIdAlreadyExists() {
        UserDTO dto = dto(null, "John", "Doe", "0001234567890");
        when(userRepository.existsByRsaId("0001234567890")).thenReturn(true);

        assertThatThrownBy(() -> usersService.addUser(dto))
                .isInstanceOf(UserFoundException.class)
                .hasMessageContaining("0001234567890");
    }

    // ─── removeUser ─────────────────────────────────────────────────────────────

    @Test
    void removeUser_deletesUser_whenUserExists() {
        when(userRepository.existsById(1L)).thenReturn(true);

        usersService.removeUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void removeUser_throwsUserNotFoundException_whenUserDoesNotExist() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> usersService.removeUser(99L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ─── getAllUsers ─────────────────────────────────────────────────────────────

    @Test
    void getAllUsers_returnsAllUsers() {
        List<User> entities = List.of(entity(1L, "A", "B", "1234567890123"));
        List<UserDTO> dtos = List.of(dto(1L, "A", "B", "1234567890123"));

        when(userRepository.findAll()).thenReturn(entities);
        when(userMapper.entityToDto(entities)).thenReturn(dtos);

        assertThat(usersService.getAllUsers()).hasSize(1);
    }

    @Test
    void getAllUsers_throwsUsersNotFoundException_whenEmpty() {
        when(userRepository.findAll()).thenReturn(List.of());
        when(userMapper.entityToDto(List.<User>of())).thenReturn(List.of());

        assertThatThrownBy(() -> usersService.getAllUsers())
                .isInstanceOf(UsersNotFoundException.class);
    }

    // ─── getUserById ─────────────────────────────────────────────────────────────

    @Test
    void getUserById_returnsUser_whenFound() {
        User entity = entity(1L, "John", "Doe", "0001234567890");
        UserDTO dto = dto(1L, "John", "Doe", "0001234567890");

        when(userRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(userMapper.entityToDto(entity)).thenReturn(dto);

        assertThat(usersService.getUserById(1L)).isEqualTo(dto);
    }

    @Test
    void getUserById_throwsUserNotFoundException_whenNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usersService.getUserById(99L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ─── getUsersByFirstName ─────────────────────────────────────────────────────

    @Test
    void getUsersByFirstName_returnsUsers_whenFound() {
        List<User> entities = List.of(entity(1L, "John", "Doe", "0001234567890"));
        List<UserDTO> dtos = List.of(dto(1L, "John", "Doe", "0001234567890"));

        when(userRepository.findByFirstName("John")).thenReturn(entities);
        when(userMapper.entityToDto(entities)).thenReturn(dtos);

        assertThat(usersService.getUsersByFirstName("John")).hasSize(1);
    }

    @Test
    void getUsersByFirstName_throwsException_whenNotFound() {
        when(userRepository.findByFirstName("Ghost")).thenReturn(List.of());
        when(userMapper.entityToDto(List.<User>of())).thenReturn(List.of());

        assertThatThrownBy(() -> usersService.getUsersByFirstName("Ghost"))
                .isInstanceOf(UsersNotFoundException.class);
    }

    // ─── getUserByRsaId ─────────────────────────────────────────────────────────

    @Test
    void getUserByRsaId_returnsUser_whenFound() {
        User entity = entity(1L, "John", "Doe", "0001234567890");
        UserDTO dto = dto(1L, "John", "Doe", "0001234567890");

        when(userRepository.findByRsaId("0001234567890")).thenReturn(Optional.of(entity));
        when(userMapper.entityToDto(entity)).thenReturn(dto);

        assertThat(usersService.getUserByRsaId("0001234567890")).isEqualTo(dto);
    }

    @Test
    void getUserByRsaId_throwsUserNotFoundException_whenNotFound() {
        when(userRepository.findByRsaId("9999999999999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usersService.getUserByRsaId("9999999999999"))
                .isInstanceOf(UserNotFoundException.class);
    }

    // ─── updateUser ─────────────────────────────────────────────────────────────

    @Test
    void updateUser_updatesFields_whenUserExists() {
        User existing = entity(1L, "Old", "Name", "0001234567890");
        // RSA ID unchanged — existsByRsaId is never reached (short-circuited by equals check)
        UserDTO update = dto(null, "New", "Name", "0001234567890");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));

        usersService.updateUser(1L, update);

        verify(userRepository).save(existing);
        assertThat(existing.getFirstName()).isEqualTo("New");
    }

    @Test
    void updateUser_throwsUserNotFoundException_whenUserDoesNotExist() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usersService.updateUser(99L, dto(null, "X", "Y", "0001234567890")))
                .isInstanceOf(UserNotFoundException.class);
    }

    // ─── searchUsers ─────────────────────────────────────────────────────────────

    @Test
    void searchUsers_returnsMatches() {
        List<User> entities = List.of(entity(1L, "John", "Doe", "0001234567890"));
        List<UserDTO> dtos = List.of(dto(1L, "John", "Doe", "0001234567890"));

        when(userRepository.search(null, "John", null, null)).thenReturn(entities);
        when(userMapper.entityToDto(entities)).thenReturn(dtos);

        assertThat(usersService.searchUsers(null, "John", null, null)).hasSize(1);
    }

    @Test
    void searchUsers_throwsException_whenNoResults() {
        when(userRepository.search(any(), any(), any(), any())).thenReturn(List.of());

        assertThatThrownBy(() -> usersService.searchUsers(null, "Ghost", null, null))
                .isInstanceOf(UsersNotFoundException.class);
    }

    // ─── helpers ────────────────────────────────────────────────────────────────

    private UserDTO dto(Long id, String firstName, String lastName, String rsaId) {
        return new UserDTO(id, firstName, lastName, rsaId);
    }

    private User entity(Long id, String firstName, String lastName, String rsaId) {
        return new User(id, firstName, lastName, rsaId, 0L);
    }
}
