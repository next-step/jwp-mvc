package core.mvc.support.exception;

public class FailedNewInstanceException extends RuntimeException {
    public FailedNewInstanceException(Class<?> clazz, Throwable cause) {
        super("Controller 인스턴스 생성 실패. class: " + clazz, cause);
    }
}
