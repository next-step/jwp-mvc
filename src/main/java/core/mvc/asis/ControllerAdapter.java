package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.tobe.HandlerExecution;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class ControllerAdapter implements HandlerExecution {

    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private final Controller controller;

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String viewName = controller.execute(request, response);
        if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            String location = viewName.substring(DEFAULT_REDIRECT_PREFIX.length());
            return ModelAndView.withRedirectView(location);
        }

        return ModelAndView.withJspView(viewName);
    }
}
