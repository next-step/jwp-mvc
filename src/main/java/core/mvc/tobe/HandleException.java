package core.mvc.tobe;

public class HandleException extends IllegalStateException {

    private static final String ERROR_MESSAGE = "handler가 정상적으로 실행되지 않았습니다.";

    public HandleException(Throwable cause) {
        super(ERROR_MESSAGE, cause);
    }
}
