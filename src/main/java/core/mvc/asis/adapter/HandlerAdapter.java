package core.mvc.asis.adapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

    boolean support(Object handler);

    void handle(Object handler, HttpServletRequest request, HttpServletResponse response);
}
