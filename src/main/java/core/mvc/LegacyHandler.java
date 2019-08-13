package core.mvc;

import core.mvc.asis.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LegacyHandler extends AbstractHandlerAdapter {
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    public LegacyHandler(Class<?> clazz) {
        super(clazz);
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String responsePath = ((Controller) handler).execute(request, response);

        return new ModelAndView(findView(responsePath));
    }

    private View findView(String uri) {
        if (uri.startsWith(DEFAULT_REDIRECT_PREFIX))
            return new RedirectView(uri);

        return new JspView(uri);
    }
}
