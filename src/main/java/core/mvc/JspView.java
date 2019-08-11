package core.mvc;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class JspView implements View {

    private static final String REDIRECT_PREFIX = "redirect:";

    private final String viewName;

    public JspView(final String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(final Map<String, ?> model,
                       final HttpServletRequest request,
                       final HttpServletResponse response) throws Exception {
        if (viewName.startsWith(REDIRECT_PREFIX)) {
            redirect(response);
            return;
        }

        forward(request, response);
    }

    private void redirect(final HttpServletResponse response) throws IOException {
        response.sendRedirect(viewName.substring(REDIRECT_PREFIX.length()));
    }

    private void forward(final HttpServletRequest request,
                         final HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(viewName)
                .forward(request, response);
    }
}
