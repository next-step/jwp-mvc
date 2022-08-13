package core.mvc.exception;

public class MethodArgumentTypeNotSupportedException extends RuntimeException {
    private static final String MESSAGE = "%s 를 %s 로 변환하는 것은 지원되지 않습니다.";

    public MethodArgumentTypeNotSupportedException(Class<?> type, Object arg) {
        super(String.format(MESSAGE, arg.toString(), type.toString()));
    }
}
