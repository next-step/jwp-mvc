package core.mvc.tobe.handler.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ArgumentResolver {
    boolean support(NamedParameter parameter);
    Object resolve(NamedParameter parameter, HttpServletRequest request, HttpServletResponse response);
}
