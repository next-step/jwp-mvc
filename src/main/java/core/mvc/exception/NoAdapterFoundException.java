package core.mvc.exception;

import javax.servlet.ServletException;

public class NoAdapterFoundException extends ServletException {
    public NoAdapterFoundException(String message) {
        super(message);
    }
}
