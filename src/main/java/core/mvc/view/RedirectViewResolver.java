package core.mvc.view;

public class RedirectViewResolver implements ViewResolver {

    public static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    @Override
    public boolean support(String viewName) {
        return viewName.startsWith(DEFAULT_REDIRECT_PREFIX);
    }

    @Override
    public View resolve(String viewName) {
        if (!support(viewName)) {
            return null;
        }

        String location = viewName.substring(DEFAULT_REDIRECT_PREFIX.length());
        return new RedirectView(location);
    }
}
