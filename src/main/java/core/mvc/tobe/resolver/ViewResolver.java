package core.mvc.tobe.resolver;

import core.mvc.tobe.view.View;

public interface ViewResolver {
    View resolveViewName(String viewName);
}
