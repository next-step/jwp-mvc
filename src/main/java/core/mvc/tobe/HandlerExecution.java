package core.mvc.tobe;

import core.mvc.ModelAndView;
import core.mvc.args.MethodParameters;
import core.mvc.args.resolver.MethodArgumentResolvers;
import java.lang.reflect.Method;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HandlerExecution {

    private final Object instance;
    private final Method method;
    private final MethodParameters parameters;

    public HandlerExecution(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
        this.parameters = new MethodParameters(method);
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Object[] args = MethodArgumentResolvers.resolveArguments(parameters, request);
        return (ModelAndView) method.invoke(instance, args);
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

        if (!Objects.equals(instance, that.instance)) {
            return false;
        }
        return Objects.equals(method, that.method);
    }

    @Override
    public int hashCode() {
        int result = instance != null ? instance.hashCode() : 0;
        result = 31 * result + (method != null ? method.hashCode() : 0);
        return result;
    }
}
