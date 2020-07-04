package core.mvc.tobe.resolver.view;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ViewResolvers {

    public static List<ViewResolver> resolvers = new ArrayList<>();

    static {
        resolvers.add(new RedirectViewResolver());
        resolvers.add(new JspViewResolver());
    }

    public static ViewResolver getResolver(ViewResolveParameter resolveParameter) {
        return resolvers.stream()
                .filter(r -> r.isSupport(resolveParameter))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("resolver가 없습니다."));
    }
}
