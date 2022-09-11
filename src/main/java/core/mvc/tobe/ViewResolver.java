package core.mvc.tobe;

import core.mvc.view.ForwardView;
import core.mvc.view.RedirectView;
import core.mvc.view.View;

public class ViewResolver {
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    public View resolve(String viewName) {
        if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            return new RedirectView(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
        }

        return new ForwardView(viewName);
    }
}
