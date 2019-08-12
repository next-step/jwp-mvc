package core.mvc.tobe.view;

public class EmptyUrlForwardViewResolver implements ViewResolver {
    @Override
    public View resolveViewName(String viewName) {
        return new ForwardView(viewName);
    }
}
