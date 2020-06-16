package core.mvc.tobe.handlermapping.exception;

public class NotFoundHandlerMappingException extends RuntimeException{
    public NotFoundHandlerMappingException() {
        super("Failed to find matched HandlerMapping!");
    }
}
