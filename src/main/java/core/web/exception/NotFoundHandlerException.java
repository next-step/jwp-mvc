package core.web.exception;

public class NotFoundHandlerException extends RuntimeException {
    public NotFoundHandlerException(String msg) {
        super(msg);
    }
}
