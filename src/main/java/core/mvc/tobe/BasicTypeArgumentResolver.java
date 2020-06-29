package core.mvc.tobe;

import core.annotation.web.RequestMapping;
import core.mvc.ModelAndView;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class BasicTypeArgumentResolver extends AbstractArgumentResolver {

    private ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    private Class clazz;
    private Method method;

    public BasicTypeArgumentResolver(final Class clazz, final Method method) {
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
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        Object[] values = new Object[parameterNames.length];

        for (int i = 0; i < parameterNames.length; i++) {
            String parameterName = parameterNames[i];
            String value = request.getParameter(parameterName);
            values[i] = value;
            for (final Class<?> parameterType : method.getParameterTypes()) {
                if (parameterType.equals(int.class)) {
                    values[i] = Integer.parseInt(value);
                }
                if (parameterType.equals(long.class)) {
                    values[i] = Long.parseLong(value);
                }
            }
        }

        return (ModelAndView) method.invoke(clazz.newInstance(), values);
    }
}
