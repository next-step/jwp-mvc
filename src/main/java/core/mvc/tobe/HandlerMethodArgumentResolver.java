package core.mvc.tobe;


import javax.servlet.http.HttpServletRequest;

public interface HandlerMethodArgumentResolver {
    boolean supports(MethodParameter methodParameter);

    Object resolveArgument(MethodParameter methodParameter, HttpServletRequest req) throws Exception;
}
