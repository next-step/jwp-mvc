package core.mvc.tobe.view;

import core.mvc.View;

public class ViewGenerator {

    static final String DEFAULT_REDIRECT_PREFIX = "redirect:";
    static final String DEFAULT_JSP_PREFIX = ".jsp";

    public static View of(String viewName) {
        if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            return new RedirectView(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
        }
        if (viewName.endsWith(DEFAULT_JSP_PREFIX)) {
            return new JspView(viewName);
        }
        return null;
    }
}
