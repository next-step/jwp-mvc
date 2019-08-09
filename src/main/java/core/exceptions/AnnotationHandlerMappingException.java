package core.exceptions;

public class AnnotationHandlerMappingException extends RuntimeException {
    public AnnotationHandlerMappingException() {
        super();
    }

    public AnnotationHandlerMappingException(String message) {
        super(message);
    }

    public AnnotationHandlerMappingException(String message, Throwable cause) {
        super(message, cause);
    }

    public AnnotationHandlerMappingException(Throwable cause) {
        super(cause);
    }
}
