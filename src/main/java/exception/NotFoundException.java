package exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends RuntimeException {

    private final String message = "해당 값을 찾지 못했습니다.";

    public NotFoundException(HttpStatus statusCode) {
        super(statusCode.toString());
    }
}
