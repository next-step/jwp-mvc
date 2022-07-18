package core.mvc.asis;

import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.tobe.HandlerExecution;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControllerHandlerExecutionAdapter implements HandlerExecution {
    private final Controller controller;

    public ControllerHandlerExecutionAdapter(final Controller controller) {
        this.controller = controller;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        String viewName = this.controller.execute(request, response);
        return new ModelAndView(new JspView(viewName));
    }
}
