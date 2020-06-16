package core.mvc.exceptions;

public class UnableToResolveException extends Exception  {
    public UnableToResolveException(String message) {
        super(message);
    }

    public UnableToResolveException(String message, Throwable cause) {
        super(message, cause);
    }
}
