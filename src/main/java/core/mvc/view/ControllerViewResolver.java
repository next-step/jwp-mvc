package core.mvc.view;

import core.mvc.ModelAndView;
import core.mvc.asis.Controller;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by iltaek on 2020/06/19 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class ControllerViewResolver implements ViewResolver {

    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    @Override
    public ModelAndView resolve(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String viewName = ((Controller) handler).execute(request, response);
        View view = getViewWithViewName(viewName, response);
        return new ModelAndView(view);
    }

    private View getViewWithViewName(String viewName, HttpServletResponse response) throws IOException {
        if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
            return DummyView.INSTANCE;
        }
        return new JspView(viewName);
    }
}
