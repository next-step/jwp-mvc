package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

public enum DataTypeMethodArgumentResolver implements HandlerMethodArgumentResolver {

    STRING_TYPE() {
        @Override
        public boolean supports(MethodParameter methodParameter) {
            return methodParameter.getType().equals(String.class);
        }

        @Override
        public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest req) {
            return req.getParameter(methodParameter.getName());
        }
    },
    INTEGER_TYPE() {
        @Override
        public boolean supports(MethodParameter methodParameter) {
            return methodParameter.getType().equals(int.class) || methodParameter.getType().equals(Integer.class);
        }

        @Override
        public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest req) {
            return Integer.parseInt(req.getParameter(methodParameter.getName()));
        }
    },
    LONG_TYPE() {
        @Override
        public boolean supports(MethodParameter methodParameter) {
            return methodParameter.getType().equals(long.class) || methodParameter.getType().equals(Long.class);
        }

        @Override
        public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest req) {
            return Long.parseLong(req.getParameter(methodParameter.getName()));
        }
    },
    BOOLEAN_TYPE() {
        @Override
        public boolean supports(MethodParameter methodParameter) {
            return methodParameter.getType().equals(boolean.class) || methodParameter.getType().equals(Boolean.class);
        }

        @Override
        public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest req) {
            return Boolean.parseBoolean(req.getParameter(methodParameter.getName()));
        }
    };

    public static DataTypeMethodArgumentResolver from(MethodParameter methodParameter) {
        return Arrays.stream(values())
                .filter(dataTypeMethodArgumentResolver -> dataTypeMethodArgumentResolver.supports(methodParameter))
                .findFirst()
                .orElseGet(() -> null);
    }
}
