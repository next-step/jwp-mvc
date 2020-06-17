package core.mvc.tobe;

import core.mvc.param.Parameters;
import core.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Objects;

public class HandlerExecution {
    private final Method method;
    private final Object instance;
    private final Parameters parameters;

    public HandlerExecution(final Method method, final Object instance) {
        if (Objects.isNull(method) || Objects.isNull(instance)) {
            throw new IllegalArgumentException("Fail to create HandlerExecution there is null parameter");
        }

        this.method = method;
        this.instance = instance;
        this.parameters = new Parameters(method);
    }

    public boolean isSupport(HttpServletRequest request) {
        return parameters.isParamsExist(request);
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object[] values = parameters.extractValues(request);

        return (ModelAndView) method.invoke(instance, values);
    }

    public Parameters getParameters() {
        return parameters;
    }
}
