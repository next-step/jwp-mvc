package core.mvc.tobe.resolver.view;

import core.mvc.tobe.view.View;

public interface ViewResolver {

    boolean isSupport(ViewResolveParameter resolveParameter);

    View resolve(ViewResolveParameter resolveParameter);
}
