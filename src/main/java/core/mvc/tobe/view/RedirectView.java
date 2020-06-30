package core.mvc.tobe.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class RedirectView implements View {
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";
    private final String viewName;

    public RedirectView(final String pathName) {
        this.viewName = pathName.substring(DEFAULT_REDIRECT_PREFIX.length());
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.sendRedirect(viewName);
    }
}
