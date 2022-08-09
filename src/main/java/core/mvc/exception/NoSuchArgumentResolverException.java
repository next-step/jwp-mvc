package core.mvc.exception;


import core.mvc.MethodParameter;

public class NoSuchArgumentResolverException extends RuntimeException {
    public static final String MESSAGE = "해당 타입에 맞는 리졸버를 찾지 못했습니다. [type: %s]";
    public NoSuchArgumentResolverException(MethodParameter methodParameter) {
        super(String.format(MESSAGE, methodParameter.getParameterType()));
    }
}
