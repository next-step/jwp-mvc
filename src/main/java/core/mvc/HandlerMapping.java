package core.mvc;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    Controller getHandler(HttpServletRequest request);

}
