package core.mvc.tobe;

import core.mvc.asis.Controller;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMapping {
    void initMapping();
    Controller findController(HttpServletRequest req);


}
