package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;

public interface RequestHandlerMapping {

    void initialize();

    Object getHandler(HttpServletRequest request);
}
