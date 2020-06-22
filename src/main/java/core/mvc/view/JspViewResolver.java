package core.mvc.view;

public class JspViewResolver implements ViewResolver {
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

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
