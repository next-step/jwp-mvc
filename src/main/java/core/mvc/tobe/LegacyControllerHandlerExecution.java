package core.mvc.tobe;

import core.mvc.JspView;
import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LegacyControllerHandlerExecution implements HandlerExecution {

    private String path;

    public LegacyControllerHandlerExecution(final String path) {
        this.path = path;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return new ModelAndView(new JspView(path));
    }
}
