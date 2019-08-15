package core.mvc.tobe.view;

import core.mvc.View;
import core.mvc.ViewResolver;
import core.mvc.asis.view.LegacyView;

/**
 * @author : yusik
 * @date : 2019-08-15
 */
public class DefaultViewResolver implements ViewResolver {

    private static final String REDIRECT_PREFIX = "redirect:";
    public static final String JSP_SUFFIX = ".jsp";

    @Override
    public View resolveViewName(String viewName) {

        if (viewName.startsWith(REDIRECT_PREFIX)) {
            return new RedirectView(viewName.substring(REDIRECT_PREFIX.length()));
        }

        if (viewName.endsWith(JSP_SUFFIX)) {
            return new JspView(viewName);
        }

        return new LegacyView(viewName);
    }
}
