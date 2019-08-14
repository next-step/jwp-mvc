package core.mvc.tobe.view;

import javax.servlet.ServletException;
import java.util.List;

import static java.util.Arrays.asList;

public class ViewResolverManager {

    private List<ViewResolver> viewResolvers;

    public ViewResolverManager() {
        this.viewResolvers = createViewResolvers();
    }

    public View resolveView(String viewName) throws ServletException {
        for (ViewResolver viewResolver : viewResolvers) {
            final View view = viewResolver.resolveViewName(viewName);
            if (view != null) {
                return view;
            }
        }

        throw new ServletException("No view Resolver for view name [" + viewName+ "]");
    }

    private List<ViewResolver> createViewResolvers() {
        return asList(
                new UrlBasedViewResolver(),
                new EmptyUrlForwardViewResolver()
        );
    }

    public void destory() {
        viewResolvers.clear();
    }
}
