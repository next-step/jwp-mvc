package core.mvc.tobe;

import core.mvc.param.Parameters;
import core.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

public class HandlerExecution {
    private final HandlerKey key;
    private final Method method;
    private final Object instance;

    public HandlerExecution(final HandlerKey key, final Method method, final Object instance) {
        if (Objects.isNull(key) || Objects.isNull(method) || Objects.isNull(instance)) {
            throw new IllegalArgumentException("Fail to create HandlerExecution there is null parameter");
        }

        this.key = key;
        this.method = method;
        this.instance = instance;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Parameters parameters = new Parameters(method);
        updateParams(request);
        //get values = ValueExtractors.getValues(parameters, request);
        //method.invoke(instance, values);
        return (ModelAndView) method.invoke(instance, request, response);
    }

    public void updateParams(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        Map<String, String> requestParams = key.parseRequestParam(requestURI);

        requestParams.keySet()
                .forEach(key -> request.setAttribute(key, requestParams.get(key)));
    }
}
