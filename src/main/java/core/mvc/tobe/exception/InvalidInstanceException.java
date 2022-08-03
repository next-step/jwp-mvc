package core.mvc.tobe.exception;

public class InvalidInstanceException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "인스턴스 초기화에 실패했습니다";

    public InvalidInstanceException(final Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}
