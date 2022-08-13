package core.mvc.exception;

public class MethodResolverNotSupportedException extends RuntimeException {

    private static final String MESSAGE = "%s 를 에 해당되는 resolver 가 존재하지 않습니다.";

    public MethodResolverNotSupportedException(Class<?> type) {
        super(String.format(MESSAGE, type.toString()));
    }
}
