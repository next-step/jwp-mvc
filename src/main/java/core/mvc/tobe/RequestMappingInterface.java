package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;

public interface RequestMappingInterface {

    void initialize();

    Object findHandler(HttpServletRequest request);
}
