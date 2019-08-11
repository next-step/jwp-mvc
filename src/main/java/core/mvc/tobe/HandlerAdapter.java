package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

    boolean supports(HttpServletRequest req);

    void handle(HttpServletRequest req, HttpServletResponse resp) throws Exception;

}
