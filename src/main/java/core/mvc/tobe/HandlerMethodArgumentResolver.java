package core.mvc.tobe;

import core.mvc.MethodParameter;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMethodArgumentResolver {
    boolean supports(MethodParameter parameter);
    Object getMethodArgument(MethodParameter parameter, HttpServletRequest request) throws Exception;


}
