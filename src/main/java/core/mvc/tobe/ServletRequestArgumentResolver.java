package core.mvc.tobe;

import core.mvc.MethodParameter;

import javax.servlet.http.HttpServletRequest;

public class ServletRequestArgumentResolver extends AbstractHandlerMethodArgumentResolver {

    @Override
    public boolean supports(MethodParameter parameter) {
        return parameter.getType().equals(HttpServletRequest.class);
    }

    @Override
    public Object getMethodArgument(MethodParameter parameter, HttpServletRequest request) throws Exception {
        return request;
    }
}
