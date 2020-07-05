package core.mvc.exception;

import javax.servlet.ServletException;

public class ReflectionsException extends ServletException {

    public ReflectionsException(String message, Throwable rootCause) {
        super(message, rootCause);
    }
}
