package za.co.example.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import za.co.example.persistence.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByFirstName(String firstName);

    List<User> findByLastName(String lastName);

    Optional<User> findByRsaId(String rsaId);

    boolean existsByRsaId(String rsaId);

    /**
     * Search users matching ANY of the provided criteria (OR logic).
     * Null parameters are ignored — only non-null values are applied as filters.
     */
    @Query("SELECT DISTINCT u FROM User u WHERE " +
           "(:id IS NOT NULL AND u.id = :id) OR " +
           "(:firstName IS NOT NULL AND LOWER(u.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) OR " +
           "(:lastName IS NOT NULL AND LOWER(u.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) OR " +
           "(:rsaId IS NOT NULL AND u.rsaId = :rsaId)")
    List<User> search(@Param("id") Long id,
                      @Param("firstName") String firstName,
                      @Param("lastName") String lastName,
                      @Param("rsaId") String rsaId);
}
