package core.mvc.exception;

public class InvalidParameterAnnotation extends RuntimeException {

    public InvalidParameterAnnotation(Class<?> annotationClass) {
        super(String.format(
                "Method Parameter Annotation : %s",
                annotationClass.getName()
        ));
    }
}
