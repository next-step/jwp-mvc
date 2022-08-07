package core.mvc.tobe.exception;

public class UnSupportedControllerInstanceException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "컨트롤러 애너테이션이 없는 클래스는 지원하지 않습니다: %s";

    public UnSupportedControllerInstanceException(String message) {
        super(String.format(DEFAULT_MESSAGE, message));
    }
}
