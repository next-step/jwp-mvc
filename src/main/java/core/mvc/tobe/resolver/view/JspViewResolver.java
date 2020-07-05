package core.mvc.tobe.resolver.view;

import core.mvc.tobe.view.JspView;
import core.mvc.tobe.view.View;

public class JspViewResolver implements ViewResolver {
    private static final String DEFAULT_JSP_EXTENSION = ".jsp";

    @Override
    public boolean isSupport(ViewResolveParameter resolveParameter) {
        String viewName = resolveParameter.getViewName();
        return viewName.endsWith(DEFAULT_JSP_EXTENSION);
    }

    @Override
    public View resolve(ViewResolveParameter resolveParameter) {
        return new JspView(resolveParameter.getViewName());
    }
}
