package core.mvc.tobe.view;

public class ViewFactory {
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    public static View create(String pathName) {
        if (pathName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            return new RedirectView(pathName);
        }
        return new JspView(pathName);
    }
}
