package core.mvc.tobe.handler.exception;

public class NotFoundHandlerException extends RuntimeException{
    public NotFoundHandlerException() {
        super("Failed to find matched Handler!");
    }
}
