package core.mvc.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ArgumentResolver {

    boolean supports(RequestParameter requestParameter);

    Object resolveArgument(RequestParameter requestParameter, HttpServletRequest request, HttpServletResponse response);

}
