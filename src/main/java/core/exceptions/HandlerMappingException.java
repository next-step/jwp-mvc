package core.exceptions;

public class HandlerMappingException extends RuntimeException {
    public HandlerMappingException() {
        super();
    }

    public HandlerMappingException(String message) {
        super(message);
    }

    public HandlerMappingException(String message, Throwable cause) {
        super(message, cause);
    }

    public HandlerMappingException(Throwable cause) {
        super(cause);
    }
}
