package core.mvc.tobe.view.exception;

public class RedirectException extends RuntimeException{
    public RedirectException(Throwable cause) {
        super("Failed to redirect!", cause);
    }
}
