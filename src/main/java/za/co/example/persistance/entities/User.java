package za.co.example.persistance.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "lst_users")
public class User {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "rsa_id")
    private String rsaId;

    public User() {
    }

    public User(Long id, String firstName, String lastName, String rsaId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.rsaId = rsaId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRsaId() {
        return rsaId;
    }

    public void setRsaId(String rsaId) {
        this.rsaId = rsaId;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", rsaId='" + rsaId + '\'' +
                '}';
    }
}
