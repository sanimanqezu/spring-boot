package za.co.example.constants;

import za.co.example.persistance.entities.User;

import java.util.ArrayList;
import java.util.List;

public class UsersConstant {

    public static List<User> users = new ArrayList<>();

    static {
        User firstUser = new User();
        firstUser.setId(1L);
        firstUser.setFirstName("John");
        firstUser.setLastName("Doe");
        firstUser.setRsaId("0001234567890");
        users.add(firstUser);

        User secondUser = new User();
        secondUser.setId(2L);
        secondUser.setFirstName("Mary");
        secondUser.setLastName("Jane");
        secondUser.setRsaId("0123456789012");
        users.add(secondUser);
    }
}
