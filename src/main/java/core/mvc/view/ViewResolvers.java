package core.mvc.view;

import java.util.Arrays;
import java.util.List;

public class ViewResolvers {

    private final List<ViewResolver> resolvers = Arrays.asList(new RedirectViewResolver(), new JspViewResolver());

    public View resolve(String viewName) {
        return resolvers.stream()
                .filter(resolver -> resolver.support(viewName))
                .map(resolver -> resolver.resolve(viewName))
                .findFirst()
                .orElse(null);
    }
}
