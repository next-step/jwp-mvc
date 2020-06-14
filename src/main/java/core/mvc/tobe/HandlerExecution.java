package core.mvc.tobe;

import core.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Objects;

public class HandlerExecution {
    private final Method method;
    private final Object instance;

    public HandlerExecution(final Method method, final Object instance) {
        if (Objects.isNull(method) || Objects.isNull(instance)) {
            throw new IllegalArgumentException("Fail to create HandlerExecution there is null parameter");
        }

        this.method = method;
        this.instance = instance;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(instance, request, response);
    }
}
