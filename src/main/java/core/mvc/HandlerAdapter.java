package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

    boolean supports(Object handler);
    void handle(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
