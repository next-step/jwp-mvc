package core.mvc.view;

import core.mvc.asis.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControllerViewResolver extends AbstractViewResolver {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(final Object handler,
                               final HttpServletRequest request,
                               final HttpServletResponse response) throws Exception {
        String viewName = ((Controller) handler).execute(request, response);
        View view = resolve(viewName, response);

        return new ModelAndView(view);
    }
}
