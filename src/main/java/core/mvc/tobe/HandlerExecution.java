package core.mvc.tobe;

import core.annotation.web.RequestMapping;
import core.mvc.ModelAndView;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;

public class HandlerExecution implements ExecuteHandler {
    private final Object declaredObject;
    private final Method method;

    public HandlerExecution(Object declaredObject, Method method) {
        this.declaredObject = declaredObject;
        this.method = method;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Object> parameters = extractParameters(request, response);
        if (parameters == null || parameters.size() < 1) {
            return getModelAndView(method.invoke(declaredObject));
        }
        return getModelAndView(method.invoke(declaredObject, parameters.toArray()));
    }

    private List<Object> extractParameters(HttpServletRequest request, HttpServletResponse response) {
        RequestParameters requestParameters = new RequestParameters(request, method);
        return requestParameters.extraction(request, response);
    }

    private ModelAndView getModelAndView(Object invokeObject) {
        if (invokeObject instanceof ModelAndView) {
            return (ModelAndView) invokeObject;
        }

        if (invokeObject instanceof String) {
            return new ModelAndView((String) invokeObject);
        }
        return null;
    }
}
