package core.mvc.tobe.resolver.view;

import core.mvc.tobe.view.RedirectView;
import core.mvc.tobe.view.View;

public class RedirectViewResolver implements ViewResolver {


    @Override
    public boolean isSupport(ViewResolveParameter resolveParameter) {
        return resolveParameter.isRedirect();
    }

    @Override
    public View resolve(ViewResolveParameter resolveParameter) {
        return new RedirectView(resolveParameter.getViewName());
    }
}
