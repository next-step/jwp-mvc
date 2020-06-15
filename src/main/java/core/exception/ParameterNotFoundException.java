package core.exception;

public class ParameterNotFoundException extends RuntimeException {
    public ParameterNotFoundException(String parameter) {
        super("Parameter [" + parameter + "] is not exist");
    }
}
