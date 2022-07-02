package core.mvc.asis;

import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.tobe.HandlerExecution;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Controller extends HandlerExecution {
    @Override
    default ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String viewName = this.execute(request, response);
        return new ModelAndView(new JspView(viewName));
    }

    String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception;
}
