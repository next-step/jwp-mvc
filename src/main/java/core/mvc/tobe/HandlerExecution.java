package core.mvc.tobe;

import core.mvc.support.MethodSignature;
import core.mvc.support.resolvers.Resolvers;
import core.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Objects;

public class HandlerExecution {

    private final Object instance;
    private final Method method;

    public HandlerExecution(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Object[] args = Resolvers.resolveArguments(new MethodSignature(method), request);
        return (ModelAndView) method.invoke(instance, args);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HandlerExecution that = (HandlerExecution) o;
        return Objects.equals(instance, that.instance) &&
                Objects.equals(method, that.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instance, method);
    }
}
