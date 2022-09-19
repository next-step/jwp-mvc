package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMapping {
    Object getHandler(HttpServletRequest request);

    default boolean hasHandler(HttpServletRequest request) {
        return getHandler(request) != null;
    }
}
