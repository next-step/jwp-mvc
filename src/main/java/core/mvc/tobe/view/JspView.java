package core.mvc.tobe.view;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class JspView implements View {
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private String mappedUri;

    public JspView(String mappedUri) {
        this.mappedUri = mappedUri;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (mappedUri.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            response.sendRedirect(mappedUri.substring(DEFAULT_REDIRECT_PREFIX.length()));
            return;
        }

        RequestDispatcher rd = request.getRequestDispatcher(mappedUri);
        rd.forward(request, response);
    }
}
