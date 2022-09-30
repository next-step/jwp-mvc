package core.mvc.tobe;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.ModelAndView;
import next.controller.UserSessionUtils;

public class ControllerExecutor implements Controller {
    private static final List<HandlerMethodArgumentResolver> RESOLVER_LIST = new ArrayList<>();

    static {
        RESOLVER_LIST.add(new HttpServletRequestMethodArgumentResolver());
        RESOLVER_LIST.add(new PrimitiveMethodArgumentResolver());
        RESOLVER_LIST.add(new WrapperMethodArgumentResolver());
        RESOLVER_LIST.add(new UserMethodArgumentResolver());
        RESOLVER_LIST.add(new PathVariableMethodArgumentResolver());
    }

    private final Object declaredObject;
    private final Method method;

    public ControllerExecutor(Object declaredObject, Method method) {
        this.declaredObject = declaredObject;
        this.method = method;
    }

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object[] parameters = resolveParameters(this.method, request);

        ModelAndView mav = (ModelAndView) method.invoke(declaredObject, parameters);

        mav.getModel().forEach((key, value) -> {
            request.getSession().setAttribute(key, value);

            if (key.equals(UserSessionUtils.USER_DELETE_KEY) && (boolean) value) {
                request.getSession().removeAttribute(UserSessionUtils.USER_SESSION_KEY);
            }
        });

        return mav;
    }

    private Object[] resolveParameters(Method method, HttpServletRequest request) {
        Parameter[] parameters = method.getParameters();
        final int length = parameters.length;
        Object[] values = new Object[length];

        for (int i = 0; i < length; i++) {
            MethodParameter methodParameter = new MethodParameter(method, i, parameters[i].getAnnotations());

            values[i] = RESOLVER_LIST.stream()
                    .filter(r -> r.supportsParameter(methodParameter))
                    .findFirst()
                    .orElse(new DefaultMethodArgumentResolver())
                    .resolveArgument(methodParameter, request);
        }

        return values;
    }
}
