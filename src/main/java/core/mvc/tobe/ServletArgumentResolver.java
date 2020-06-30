package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class ServletArgumentResolver extends ArgumentResolver {

    private Class clazz;
    private Method method;

    public ServletArgumentResolver(final Class clazz, final Method method) {
        this.clazz = clazz;
        this.method = method;
        applyExecution(method, this);
    }

    @Override
    public void applyExecution(final Method method, final HandlerExecution handlerExecution) {
        super.applyExecution(method, handlerExecution);
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return super.handle(request, response);
    }
}
