package core.mvc.tobe.resolver.view;

public class ViewResolveParameter {
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private String viewName;

    public ViewResolveParameter(String viewName) {
        this.viewName = viewName;
    }

    public boolean isRedirect() {
        return viewName.startsWith(DEFAULT_REDIRECT_PREFIX);
    }

    public String getViewName() {
        return viewName.replace(DEFAULT_REDIRECT_PREFIX, "");
    }
}
