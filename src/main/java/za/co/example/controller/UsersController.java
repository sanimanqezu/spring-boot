package za.co.example.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import za.co.example.core.service.IUsersService;
import za.co.example.dto.UserDTO;

import java.util.List;

@RestController
@RequestMapping(value = "/users", produces = "application/json")
@RequiredArgsConstructor
@Validated
public class UsersController {

    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    private final IUsersService usersService;

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Void> addUser(@Valid @RequestBody UserDTO user) {
        logger.info("Creating user with RSA ID: {}", user.getRsaId());
        usersService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        logger.info("Retrieving all users");
        return ResponseEntity.ok(usersService.getAllUsers());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        logger.info("Retrieving user by id: {}", id);
        return ResponseEntity.ok(usersService.getUserById(id));
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> removeUserById(@PathVariable Long id) {
        logger.info("Removing user by id: {}", id);
        usersService.removeUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/firstName")
    public ResponseEntity<List<UserDTO>> getUsersByFirstName(@RequestParam String firstName) {
        logger.info("Retrieving users by firstName: {}", firstName);
        return ResponseEntity.ok(usersService.getUsersByFirstName(firstName));
    }

    @GetMapping("/lastName")
    public ResponseEntity<List<UserDTO>> getUsersByLastName(@RequestParam String lastName) {
        logger.info("Retrieving users by lastName: {}", lastName);
        return ResponseEntity.ok(usersService.getUsersByLastName(lastName));
    }

    @GetMapping("/rsaId")
    public ResponseEntity<UserDTO> getUserByRsaId(
            @RequestParam
            @Pattern(regexp = "^[0-9]{13}$", message = "RSA ID must be exactly 13 numeric digits")
            String rsaId) {
        logger.info("Retrieving user by RSA ID");
        return ResponseEntity.ok(usersService.getUserByRsaId(rsaId));
    }

    @PutMapping(value = "/id/{id}", consumes = "application/json")
    public ResponseEntity<Void> updateUserById(@PathVariable Long id,
                                               @Valid @RequestBody UserDTO updatedUser) {
        logger.info("Updating user with id: {}", id);
        usersService.updateUser(id, updatedUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> searchUsers(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String rsaId) {
        logger.info("Searching users — id: {}, firstName: {}, lastName: {}, rsaId provided: {}",
                id, firstName, lastName, rsaId != null);
        return ResponseEntity.ok(usersService.searchUsers(id, firstName, lastName, rsaId));
    }
}
