package core.mvc.tobe.view;

public class UrlBasedViewResolver implements ViewResolver {

    @Override
    public View resolveViewName(String viewName) {
        if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            return new RedirectView(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
        }

        if (viewName.startsWith(DEFAULT_FORWARD_PREFIX)) {
            return new ForwardView(viewName.substring(DEFAULT_FORWARD_PREFIX.length()));
        }

        return null;
    }
}
