package core.mvc;

import javax.servlet.http.HttpServletRequest;

public interface Mapping {

    void initialize();
    Object getHandler(HttpServletRequest request);

}
