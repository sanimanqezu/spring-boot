package za.co.example.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import za.co.example.core.service.IUsersService;
import za.co.example.persistance.entities.User;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/users")
public class UsersController {

    private final Logger logger = LoggerFactory.getLogger(UsersController.class);

    private final IUsersService usersService;

    @PostMapping(produces = "application/json")
    public ResponseEntity<Void> addUser(@RequestBody User user) {
        logger.info("Adding user: {}", user.toString());
        usersService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<User>> getAllUsers() {
        logger.info("Retrieving all users");
        return ResponseEntity.ok(usersService.getAllUsers());
    }

    @GetMapping(value = "/id/{id}", produces = "application/json")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        logger.info("Retrieving a user by Id: {}", id);
        return ResponseEntity.ok(usersService.getUserById(id));
    }

    @DeleteMapping(value = "/id/{id}", produces = "application/json")
    public ResponseEntity<Void> removeUserById(@PathVariable Long id) {
        logger.info("Removing a user by Id: {}", id);
        usersService.removeUser(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(value = "/firstName", produces = "application/json")
    public ResponseEntity<List<User>> getUsersByFirstName(@RequestParam String firstName) {
        logger.info("Retrieving user(s) by first name: {}", firstName);
        return ResponseEntity.ok(usersService.getUsersByFirstName(firstName));
    }

    @GetMapping(value = "/lastName", produces = "application/json")
    public ResponseEntity<List<User>> getUsersByLastName(@RequestParam String lastName) {
        logger.info("Retrieving user(s) by last name: {}", lastName);
        return ResponseEntity.ok(usersService.getUsersByLastName(lastName));
    }

    @GetMapping(value = "/rsaId", produces = "application/json")
    public ResponseEntity<User> getUserByRsaId(@RequestParam String rsaId) {
        logger.info("Retrieving a user by RSA Id: {}", rsaId);
        return ResponseEntity.ok(usersService.getUserByRsaId(rsaId));
    }

    @PutMapping(value = "/id/{id}", produces = "application/json")
    public ResponseEntity<Void> updateUserById(@PathVariable Long id, @RequestBody User updatedUser) {
        logger.info("Updating user identified by id: {}, with user: {}", id, updatedUser.toString());
        usersService.updateUser(id, updatedUser);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(value = "/search", produces = "application/json")
    public ResponseEntity<List<User>> searchUses(@RequestParam(value = "id", required = false) Long id,
                                                 @RequestParam(value = "firstName", required = false) String firstName,
                                                 @RequestParam(value = "lastName", required = false) String lastName,
                                                 @RequestParam(value = "rsaId", required = false) String rsaId) {
        logger.info("Searching user(s) by the following properties: \n Id: {} \n First Name: {} \n Last Name: {} \n RSA Id: {}",
                id, firstName, lastName, rsaId);
        return ResponseEntity.ok(usersService.searchUsers(id, firstName, lastName, rsaId));
    }
}
