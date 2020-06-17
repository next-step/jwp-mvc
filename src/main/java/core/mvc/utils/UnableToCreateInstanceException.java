package core.mvc.utils;

public final class UnableToCreateInstanceException extends Exception {
    public UnableToCreateInstanceException(String message) {
        super(message);
    }

    public UnableToCreateInstanceException(String message, Throwable cause) {
        super(message, cause);
    }
}
