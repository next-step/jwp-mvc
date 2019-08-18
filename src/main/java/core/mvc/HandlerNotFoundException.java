package core.mvc;

public class HandlerNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE = "핸들러를 찾을 수 없습니다.";

    public HandlerNotFoundException() {
        super(ERROR_MESSAGE);
    }
}
