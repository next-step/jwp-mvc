package core.mvc.tobe;

public class IllegalMethodException extends IllegalArgumentException {

    private static final String MESSAGE = "invalid method: (%s)";

    public IllegalMethodException(String method) {
        super(String.format(MESSAGE, method));
    }
}
