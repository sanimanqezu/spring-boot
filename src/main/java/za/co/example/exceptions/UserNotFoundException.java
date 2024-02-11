package za.co.example.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

    private final Object var;
    private final Object val;

    public UserNotFoundException(Object var, Object val) {
        super("User with " + var + " = " + val + " was not found");
        this.var = var;
        this.val = val;
    }

    public UserNotFoundException(String message) {
        super(message);
        this.var = null;
        this.val = null;
    }

    public Object getVar() {
        return var;
    }

    public Object getVal() {
        return val;
    }
}
