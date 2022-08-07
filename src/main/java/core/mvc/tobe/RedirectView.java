package core.mvc.tobe;

import core.mvc.View;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RedirectView implements View {

    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private final String redirectUrl;

    public RedirectView(final String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if (redirectUrl.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            response.sendRedirect(redirectUrl.substring(DEFAULT_REDIRECT_PREFIX.length()));
            return;
        }

        response.sendRedirect(redirectUrl);
    }
}
