package core.mvc.tobe;

import core.mvc.ModelAndView;
import java.lang.reflect.Method;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HandlerExecution implements HandlerExecutable {

    private final Object handler;
    private final Method method;

    public HandlerExecution(final Object handler, final Method method) {
        this.handler = handler;
        this.method = method;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(handler, request, response);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final HandlerExecution that = (HandlerExecution) o;
        return Objects.equals(handler, that.handler) && Objects.equals(method, that.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(handler, method);
    }
}
