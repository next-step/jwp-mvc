package core.mvc.exception;

import static core.mvc.exception.ErrorMessage.NOT_PRESENT_REQUEST_MAPPING;

import java.lang.reflect.Method;

public class NotPresentRequestMappingException extends RuntimeException{
    public NotPresentRequestMappingException(Method method) {
        super(String.format(NOT_PRESENT_REQUEST_MAPPING.getMessage(), method.getName()));
    }
}
