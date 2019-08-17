package core.mvc;

public final class ViewFactory {

    private static final String REDIRECT_PREFIX = "redirect:";

    private ViewFactory() { }

    public static View create(final String viewName) {
        if (viewName.startsWith(REDIRECT_PREFIX)) {
            return RedirectView.of(viewName.substring(REDIRECT_PREFIX.length()));
        }

        return JspView.of(viewName);
    }
}
