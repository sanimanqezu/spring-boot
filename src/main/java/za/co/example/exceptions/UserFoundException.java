package za.co.example.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserFoundException extends RuntimeException {

    private final Object var;
    private final Object val;

    public UserFoundException(Object var, Object val) {
        super("User with " + var + " = " + val + " already exists");
        this.var = var;
        this.val = val;
    }

    public UserFoundException(String message) {
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
