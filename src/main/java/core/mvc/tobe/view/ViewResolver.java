package core.mvc.tobe.view;

public interface ViewResolver {

    String DEFAULT_REDIRECT_PREFIX = "redirect:";

    String DEFAULT_FORWARD_PREFIX = "forward:";

    String HANDLE_BARS_EXTENSION = "hbs";

    View resolveViewName(String viewName);

}
