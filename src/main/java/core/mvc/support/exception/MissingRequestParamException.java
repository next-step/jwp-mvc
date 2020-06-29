package core.mvc.support.exception;

public class MissingRequestParamException extends RuntimeException {
    public MissingRequestParamException(String parameterName) {
        super("파라미터(" + parameterName + ")의 값이 비어있습니다.");
    }
}
