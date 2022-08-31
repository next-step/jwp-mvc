package core.mvc.exception;

import static core.mvc.exception.ErrorMessage.METHOD_ARGUMENT_TYPE_NOT_SUPORT;

public class MethodArgumentTypeNotSupportedException extends RuntimeException {
    public MethodArgumentTypeNotSupportedException(Class<?> type, Object arg) {
        super(String.format(METHOD_ARGUMENT_TYPE_NOT_SUPORT.getMessage(), arg.toString(), type.toString()));
    }
}
