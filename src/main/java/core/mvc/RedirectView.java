package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class RedirectView implements View {
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private String uri;

    public RedirectView(String uri) {
        this.uri = uri;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.sendRedirect(uri.substring(DEFAULT_REDIRECT_PREFIX.length()));
    }
}
