package core.mvc.tobe.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ArgumentResolver {
    Object resolve(HttpServletRequest request, HttpServletResponse response, ArgumentModel argumentModel);

    boolean isSupport(ArgumentModel argumentModel);
}
