package core.mvc.exception;

public class ArgumentResolverException extends Exception {
    public ArgumentResolverException(Exception e) {
        super(e.getMessage());
    }

    public ArgumentResolverException(String message) {
        super(message);
    }
}
