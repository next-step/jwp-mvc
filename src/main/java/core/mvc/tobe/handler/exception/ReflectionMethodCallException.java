package core.mvc.tobe.handler.exception;

public class ReflectionMethodCallException extends RuntimeException{
    public ReflectionMethodCallException(Throwable cause) {
        super("Failed to call method using reflection!", cause);
    }
}
