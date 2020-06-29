package core.mvc.support.exception;

public class HandlerNotFoundException extends RuntimeException {
    public HandlerNotFoundException(String requestURI, String httpMethod) {
        super("핸들러를 찾을 수 없습니다. requestURI: " + requestURI + " , httpMethod: " + httpMethod);
    }
}
