package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;

public interface RequestMapping {

    void initialize();

    Object findHandler(HttpServletRequest request);
}
