package za.co.example.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private int status;
    private String message;
    private Instant timestamp;
    private String path;

    public ErrorResponse(int status, String message, String path) {
        this.status = status;
        this.message = message;
        this.timestamp = Instant.now();
        this.path = path;
    }
}
