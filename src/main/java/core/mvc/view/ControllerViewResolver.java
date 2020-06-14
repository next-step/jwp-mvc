package core.mvc.view;

import core.mvc.asis.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControllerViewResolver extends AbstractViewResolver {

    @Override
    public ModelAndView handle(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!(handler instanceof Controller)) {
            return null;
        }

        String viewName = ((Controller) handler).execute(request, response);
        View view = resolve(viewName, response);

        return new ModelAndView(view);
    }
}
