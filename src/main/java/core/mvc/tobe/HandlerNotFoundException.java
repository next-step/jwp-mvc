package core.mvc.tobe;

public class HandlerNotFoundException extends RuntimeException {

    private static final String MESSAGE = "not found handler uri: (%s)";

    public HandlerNotFoundException(String handler) {
        super(String.format(MESSAGE, handler));
    }
}
