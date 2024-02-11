package za.co.example.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.example.persistance.entities.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByFirstName(String firstName);

    List<User> findByLastName(String lastName);

    User findByRsaId(String rsaId);
}
