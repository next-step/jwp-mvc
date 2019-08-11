package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class RedirectView implements View {

    public static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private final String viewName;

    public RedirectView(String viewName) {
        if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            viewName = viewName.substring(DEFAULT_REDIRECT_PREFIX.length());
        }
        this.viewName = viewName;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.sendRedirect(viewName);
    }
}
