package core.mvc.tobe;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerMethodArgumentResolver {
    boolean supports(MethodParameter methodParameter);

    Object resolveArgument(MethodParameter methodParameter, HttpServletRequest req, HttpServletResponse resp) throws Exception;
}
