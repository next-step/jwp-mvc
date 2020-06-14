package core.mvc.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AbstractViewResolver implements ViewResolver {
    protected static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    @Override
    public abstract ModelAndView handle(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception;

    protected View resolve(final String viewName,
                           final HttpServletResponse response) throws IOException {
        if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
            return DummyView.INSTANCE;
        }

        return new JspView(viewName);
    }
}
