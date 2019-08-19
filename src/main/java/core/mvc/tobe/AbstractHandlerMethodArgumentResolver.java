package core.mvc.tobe;

import core.mvc.MethodParameter;

public abstract class AbstractHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    protected Object getArgument(MethodParameter methodParameter, String value) throws Exception {
        if (methodParameter.getType().equals(int.class)) {
            return Integer.parseInt(value);
        }

        if (methodParameter.getType().equals(Integer.class)) {
            return Integer.parseInt(value);
        }

        if (methodParameter.getType().equals(long.class)) {
            return Long.parseLong(value);
        }

        if (methodParameter.getType().equals(Long.class)) {
            return Long.parseLong(value);
        }

        if (methodParameter.getType().equals(String.class)) {
            return value;
        }

        throw new Exception("Type TypeMismatch");
    }
}
