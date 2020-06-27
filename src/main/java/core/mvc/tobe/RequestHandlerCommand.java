package core.mvc.tobe;

import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.asis.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestHandlerCommand implements HandlerCommand {

    private HttpServletRequest request;
    private HttpServletResponse response;

    public RequestHandlerCommand(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public ModelAndView execute(Object handler) throws Exception {
        String viewName = ((Controller) handler).execute(request, response);
        return new ModelAndView(new JspView(viewName));
    }
}
