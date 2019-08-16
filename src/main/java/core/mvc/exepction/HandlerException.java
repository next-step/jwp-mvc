package core.mvc.exepction;

public class HandlerException extends RuntimeException {
    public HandlerException(Throwable cause) {
        super(cause);
    }

    public HandlerException(String message, Throwable e) {
        super(message, e);
    }
}
