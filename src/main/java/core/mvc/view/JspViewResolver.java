package core.mvc.view;

import static core.mvc.view.RedirectView.DEFAULT_REDIRECT_PREFIX;

public class JspViewResolver implements ViewResolver {

    private final Class<? extends JspView> viewClass = JspView.class;

    @Override
    public boolean supports(Class<?> arg0) {
        return viewClass.isAssignableFrom(arg0);
    }

    @Override
    public View resolveViewName(String viewName) {
        if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            return new RedirectView(viewName);
        }
        return new JspView(viewName);
    }
}
