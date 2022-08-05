package core.mvc.tobe.adapter;

public class NotExistAdapterException extends RuntimeException {
    public NotExistAdapterException() {
        super();
    }

    public NotExistAdapterException(String message) {
        super(message);
    }

    public NotExistAdapterException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotExistAdapterException(Throwable cause) {
        super(cause);
    }

    protected NotExistAdapterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
