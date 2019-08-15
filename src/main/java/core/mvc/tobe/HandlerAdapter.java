package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerAdapter {
    public boolean support(HttpServletRequest req);

    public void handle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception;
}
