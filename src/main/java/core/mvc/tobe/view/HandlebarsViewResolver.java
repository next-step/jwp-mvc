package core.mvc.tobe.view;

import org.apache.commons.lang3.StringUtils;

public class HandlebarsViewResolver implements ViewResolver {
    @Override
    public View resolveViewName(String viewName) {
        final String extension = StringUtils.substringAfterLast(viewName, ".");

        if (HANDLE_BARS_EXTENSION.equals(extension)) {
            return new HandlebarsView(StringUtils.substringBeforeLast(viewName, "."));
        }

        return null;
    }
}
