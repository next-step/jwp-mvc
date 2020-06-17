package core.mvc.tobe;

import core.mvc.param.Parameters;
import core.mvc.param.extractor.context.ContextValueExtractor;
import core.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HandlerExecution {
    private final HandlerKey key;
    private final Method method;
    private final Object instance;
    private final Parameters parameters;

    public HandlerExecution(final HandlerKey key, final Method method, final Object instance) {
        if (Objects.isNull(key) || Objects.isNull(method) || Objects.isNull(instance)) {
            throw new IllegalArgumentException("Fail to create HandlerExecution there is null parameter");
        }

        this.key = key;
        this.method = method;
        this.instance = instance;
        this.parameters = new Parameters(method);
    }

    public boolean isSupport(HttpServletRequest request) {
        return parameters.isParamsExist(request);
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        updateParams(request, response);
        Object[] values = parameters.extractValues(request);

        return (ModelAndView) method.invoke(instance, values);
    }

    public void updateParams(HttpServletRequest request, HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        Map<String, String> requestParams = key.parseRequestParam(requestURI);

        requestParams.keySet()
                .forEach(key -> request.setAttribute(key, requestParams.get(key)));
    }
}
