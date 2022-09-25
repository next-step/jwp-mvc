package core.mvc.tobe.exception;

public class HandlerException extends RuntimeException {
    public HandlerException() {
        super("요청을 처리할 수 없습니다.");
    }

    public HandlerException(String message) {
        super(message);
    }
}
