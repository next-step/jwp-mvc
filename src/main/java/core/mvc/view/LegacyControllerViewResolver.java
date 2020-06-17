package core.mvc.view;

import core.mvc.asis.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * See {@link core.mvc.asis.Controller}
 */
public class LegacyControllerViewResolver implements ViewResolver {

    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    @Override
    public boolean isSupports(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView resolve(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final String viewName = ((Controller) handler).execute(request, response);
        final View view = determineViewByName(viewName, response);
        return new ModelAndView(view);
    }

    private View determineViewByName(String viewName, HttpServletResponse response) throws IOException {
        if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
            return NoView.INSTANCE;
        }
        return new JspView(viewName);
    }

}
