package za.co.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import za.co.example.core.service.IUsersService;
import za.co.example.dto.UserDTO;
import za.co.example.exceptions.GlobalExceptionHandler;
import za.co.example.exceptions.UserNotFoundException;
import za.co.example.exceptions.UsersNotFoundException;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
    value = UsersController.class,
    excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class}
)
@Import(GlobalExceptionHandler.class)
@ActiveProfiles("test")
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IUsersService usersService;

    // ─── POST /users ─────────────────────────────────────────────────────────────

    @Test
    void addUser_returns201_whenValid() throws Exception {
        UserDTO dto = new UserDTO(null, "John", "Doe", "0001234567890");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(usersService).addUser(any(UserDTO.class));
    }

    @Test
    void addUser_returns400_whenFirstNameBlank() throws Exception {
        UserDTO dto = new UserDTO(null, "", "Doe", "0001234567890");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verify(usersService, never()).addUser(any());
    }

    @Test
    void addUser_returns400_whenRsaIdInvalid() throws Exception {
        UserDTO dto = new UserDTO(null, "John", "Doe", "123");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    // ─── GET /users ───────────────────────────────────────────────────────────────

    @Test
    void getAllUsers_returns200_withUserList() throws Exception {
        List<UserDTO> users = List.of(new UserDTO(1L, "John", "Doe", "0001234567890"));
        when(usersService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"));
    }

    @Test
    void getAllUsers_returns404_whenNoUsersExist() throws Exception {
        when(usersService.getAllUsers()).thenThrow(new UsersNotFoundException("No users found"));

        mockMvc.perform(get("/users"))
                .andExpect(status().isNotFound());
    }

    // ─── GET /users/id/{id} ───────────────────────────────────────────────────────

    @Test
    void getUserById_returns200_whenFound() throws Exception {
        UserDTO user = new UserDTO(1L, "John", "Doe", "0001234567890");
        when(usersService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/users/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.rsaId").value("0001234567890"));
    }

    @Test
    void getUserById_returns404_whenNotFound() throws Exception {
        when(usersService.getUserById(99L)).thenThrow(new UserNotFoundException("id", 99L));

        mockMvc.perform(get("/users/id/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUserById_returns400_whenIdIsNotANumber() throws Exception {
        mockMvc.perform(get("/users/id/abc"))
                .andExpect(status().isBadRequest());
    }

    // ─── DELETE /users/id/{id} ───────────────────────────────────────────────────

    @Test
    void removeUserById_returns204_whenDeleted() throws Exception {
        mockMvc.perform(delete("/users/id/1"))
                .andExpect(status().isNoContent());

        verify(usersService).removeUser(1L);
    }

    @Test
    void removeUserById_returns404_whenNotFound() throws Exception {
        doThrow(new UserNotFoundException("id", 99L)).when(usersService).removeUser(99L);

        mockMvc.perform(delete("/users/id/99"))
                .andExpect(status().isNotFound());
    }

    // ─── PUT /users/id/{id} ───────────────────────────────────────────────────────

    @Test
    void updateUserById_returns200_whenUpdated() throws Exception {
        UserDTO update = new UserDTO(null, "Updated", "Name", "0001234567890");

        mockMvc.perform(put("/users/id/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk());

        verify(usersService).updateUser(eq(1L), any(UserDTO.class));
    }

    // ─── GET /users/search ───────────────────────────────────────────────────────

    @Test
    void searchUsers_returns200_withResults() throws Exception {
        List<UserDTO> users = List.of(new UserDTO(1L, "John", "Doe", "0001234567890"));
        when(usersService.searchUsers(null, "John", null, null)).thenReturn(users);

        mockMvc.perform(get("/users/search").param("firstName", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"));
    }

    // ─── GET /users/firstName ────────────────────────────────────────────────────

    @Test
    void getUsersByFirstName_returns200() throws Exception {
        when(usersService.getUsersByFirstName("Mary"))
                .thenReturn(List.of(new UserDTO(2L, "Mary", "Jane", "0123456789012")));

        mockMvc.perform(get("/users/firstName").param("firstName", "Mary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lastName").value("Jane"));
    }
}
