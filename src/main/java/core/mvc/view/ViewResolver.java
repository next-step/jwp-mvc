package core.mvc.view;

public class ViewResolver {

    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    public static View resolve(String viewName) {
        if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            return new RedirectView(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
        }
        return new JspView(viewName);
    }
}
