package core.mvc.exception;

import static core.mvc.exception.ErrorMessage.INVALID_PARAMETER_ANNOTAION;

public class InvalidParameterAnnotation extends RuntimeException{
    public InvalidParameterAnnotation(Class<?> annotationClass) {
        super(String.format(INVALID_PARAMETER_ANNOTAION.getMessage(), annotationClass.getName()));
    }
}
