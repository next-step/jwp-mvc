package core.mvc.tobe.resolver.view;

import core.mvc.tobe.view.RedirectView;
import core.mvc.tobe.view.View;

public class RedirectViewResolver implements ViewResolver {
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    @Override
    public boolean isSupport(ViewResolveParameter resolveParameter) {
        String viewName = resolveParameter.getViewName();
        return viewName.startsWith(DEFAULT_REDIRECT_PREFIX);
    }

    @Override
    public View resolve(ViewResolveParameter resolveParameter) {
        String viewName = resolveParameter.getViewName();
        viewName = viewName.replace(DEFAULT_REDIRECT_PREFIX, "");
        return new RedirectView(viewName);
    }
}
