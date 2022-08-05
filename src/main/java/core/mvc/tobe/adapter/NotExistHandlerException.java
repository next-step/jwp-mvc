package core.mvc.tobe.adapter;

public class NotExistHandlerException extends RuntimeException {
    public NotExistHandlerException() {
        super();
    }

    public NotExistHandlerException(String message) {
        super(message);
    }

    public NotExistHandlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotExistHandlerException(Throwable cause) {
        super(cause);
    }

    protected NotExistHandlerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
