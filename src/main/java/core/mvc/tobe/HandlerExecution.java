package core.mvc.tobe;

import core.mvc.MethodParameters;
import core.mvc.ModelAndView;
import core.mvc.PathVariables;
import core.mvc.RequestParameters;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Getter
public class HandlerExecution {
    private Object object;
    private Method method;
    private String path;
    private MethodParameters methodParameters;

    public HandlerExecution(Object object, Method method, String path) {
        this.object = object;
        this.method = method;
        this.path = path;
        this.methodParameters = new MethodParameters(method);
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        RequestParameters requestParameters = new RequestParameters(request);
        PathVariables pathVariables = new PathVariables(this.path, request.getRequestURI());

        Object[] args = this.methodParameters.getArgs(request, response, requestParameters, pathVariables);
        return (ModelAndView) method.invoke(this.object, args);
    }
}
