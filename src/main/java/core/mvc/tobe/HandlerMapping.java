package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMapping {
    public void initialize();
    public Object getHandler(HttpServletRequest req);
}
