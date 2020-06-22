package core.mvc;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;

public class JspView implements View {

    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private final String viewName;

    public JspView(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (isRedirect()) {
            response.sendRedirect(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
            return;
        }

        final RequestDispatcher rd = request.getRequestDispatcher(viewName);
        rd.forward(request, response);
    }

    private boolean isRedirect() {
        return Optional.ofNullable(viewName)
                .map(v -> v.startsWith(DEFAULT_REDIRECT_PREFIX))
                .orElse(false);
    }

}
