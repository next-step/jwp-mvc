package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static core.mvc.tobe.ParameterUtils.decideParameter;

public class RequestParameterArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean support(MethodParameter methodParameter) {
        return methodParameter.isRequestParameterType();
    }

    @Override
    public Object resolve(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        return decideParameter(request.getParameter(methodParameter.getParameterName()), methodParameter.getType());
    }

}
