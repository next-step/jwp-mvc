package core.mvc.exception;

import static core.mvc.exception.ErrorMessage.METHOD_RESOLVER_NOT_SUPPORT;

public class MethodResolverNotSupportedException extends RuntimeException {
    public MethodResolverNotSupportedException(Class<?> type) {
        super(String.format(METHOD_RESOLVER_NOT_SUPPORT.getMessage(), type.toString()));
    }
}
