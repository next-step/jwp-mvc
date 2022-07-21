package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;

public abstract class HandlerMapping {
    public abstract Object getHandler(HttpServletRequest request);

    public boolean hasHandler(HttpServletRequest request) {
        return getHandler(request) != null;
    }
}
