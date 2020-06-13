package core.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String url) {
        super("404 Not found exception : " + url);
    }
}
