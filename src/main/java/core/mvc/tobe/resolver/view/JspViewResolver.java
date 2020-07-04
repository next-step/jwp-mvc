package core.mvc.tobe.resolver.view;

import core.mvc.tobe.view.JspView;
import core.mvc.tobe.view.View;

public class JspViewResolver implements ViewResolver {

    @Override
    public boolean isSupport(ViewResolveParameter resolveParameter) {
        return !resolveParameter.isRedirect();
    }

    @Override
    public View resolve(ViewResolveParameter resolveParameter) {
        return new JspView(resolveParameter.getViewName());
    }
}
