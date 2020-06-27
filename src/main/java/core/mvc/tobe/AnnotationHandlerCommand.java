package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AnnotationHandlerCommand implements HandlerCommand {

    private HttpServletRequest request;
    private HttpServletResponse response;

    public AnnotationHandlerCommand(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public ModelAndView execute(Object handler) throws Exception {
        return ((HandlerExecution) handler).handle(request, response);
    }
}
