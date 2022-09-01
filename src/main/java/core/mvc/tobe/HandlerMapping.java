package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    void initMapping();
    Object getHandler(HttpServletRequest request);

    boolean isSupported(HttpServletRequest request);
}
