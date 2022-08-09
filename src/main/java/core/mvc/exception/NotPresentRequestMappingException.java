package core.mvc.exception;

import java.lang.reflect.Method;

public class NotPresentRequestMappingException extends RuntimeException {
    private static final String MESSAGE = "%s 메서드에 RequestMapping 어노테이션이 붙어있지 않습니다..";

    public NotPresentRequestMappingException(Method method) {
        super(String.format(MESSAGE, method.getName()));
    }
}
