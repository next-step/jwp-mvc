package core.mvc.exception;

import javax.servlet.ServletException;

public class NoHandlerFoundException extends ServletException {
    public NoHandlerFoundException(String message) {
        super(message);
    }
}
