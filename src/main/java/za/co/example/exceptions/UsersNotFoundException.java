package za.co.example.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UsersNotFoundException extends RuntimeException {

    public UsersNotFoundException(Object var, Object val) {
        super("No users found with " + var + " = " + val);
    }

    public UsersNotFoundException(String message) {
        super(message);
    }
}
