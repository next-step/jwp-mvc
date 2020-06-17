package core.mvc.tobe.view.exception;

public class ForwardException extends RuntimeException{
    public ForwardException(Throwable cause) {
        super("Failed to forward!", cause);
    }
}
