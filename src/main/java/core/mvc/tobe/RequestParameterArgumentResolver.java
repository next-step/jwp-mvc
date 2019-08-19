package core.mvc.tobe;

import core.mvc.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class RequestParameterArgumentResolver extends AbstractHandlerMethodArgumentResolver {

    @Override
    public boolean supports(MethodParameter parameter) {
        return parameter.isAnnotationNotExist();
    }

    @Override
    public Object getMethodArgument(MethodParameter parameter, HttpServletRequest request) throws Exception {
        if (parameter.getType().equals(HttpSession.class)) {
            return request.getSession();
        }

        return getArgument(parameter, request.getParameter(parameter.getName()));
    }
}
