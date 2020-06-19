package core.mvc.tobe;

/**
 * @author KingCjy
 */
public class HandlerNotSupportException extends RuntimeException {

    private Class<?> type;

    public HandlerNotSupportException(String message, Class<?> type) {
        super(message);
        this.type = type;
    }
}
