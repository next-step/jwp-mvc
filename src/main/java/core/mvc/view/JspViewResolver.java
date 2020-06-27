package core.mvc.view;

public class JspViewResolver implements ViewResolver {

    private static final String JSP_VIEW_SUFFIX = ".jsp";

    @Override
    public boolean support(String viewName) {
        return viewName.endsWith(JSP_VIEW_SUFFIX);
    }

    @Override
    public View resolve(String viewName) {
        return new JspView(viewName);
    }
}
