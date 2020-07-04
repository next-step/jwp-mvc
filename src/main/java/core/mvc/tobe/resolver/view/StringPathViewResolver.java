package core.mvc.tobe.resolver.view;

import core.mvc.tobe.view.JspView;
import core.mvc.tobe.view.RedirectView;
import core.mvc.tobe.view.View;

public class StringPathViewResolver implements ViewResolver {
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    @Override
    public View resolveViewName(String viewName) {
        if (isRedirectView(viewName)) {
            String redirectViewName = viewName.substring(DEFAULT_REDIRECT_PREFIX.length());
            return new RedirectView(redirectViewName);
        }
        return new JspView(viewName);
    }

    private boolean isRedirectView(String viewName) {
        return viewName.startsWith(DEFAULT_REDIRECT_PREFIX);
    }
}
