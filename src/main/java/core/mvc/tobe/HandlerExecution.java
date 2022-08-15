package core.mvc.tobe;

import java.lang.reflect.Method;
import java.util.Objects;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HandlerExecution {

    private final Object instance;
    private final Method method;

    public HandlerExecution(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(instance, request, response);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HandlerExecution that = (HandlerExecution) o;
        return Objects.equals(instance, that.instance) && Objects.equals(method, that.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instance, method);
    }
}
