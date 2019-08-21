package core.mvc.tobe;

import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.RedirectView;
import core.mvc.View;
import core.mvc.asis.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControllerHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean canHandle(Object target) {
        return target instanceof Controller;
    }

    @Override
    public ModelAndView handle(Object target, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Controller controller = (Controller) target;
        String viewName = controller.execute(request, response);
        return new ModelAndView(parseView(viewName));
    }

    private View parseView(String viewName) {
        if (RedirectView.isRedirect(viewName)) {
            return new RedirectView(viewName);
        }
        return new JspView(viewName);
    }
}
