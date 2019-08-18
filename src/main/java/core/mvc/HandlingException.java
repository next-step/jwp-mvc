package core.mvc;

public class HandlingException extends RuntimeException {
    
    private static final String ERROR_MESSAGE = "요청 처리 중 알 수 없는 문제가 발생했습니다.";

    public HandlingException(final Throwable cause) {
        super(ERROR_MESSAGE, cause);
    }
}
