package core.mvc;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class JspView implements View {
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private final String viewName;

    public JspView(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (this.viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            response.sendRedirect(this.viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
            return;
        }

        for (final Map.Entry<String, ?> modelAttribute: model.entrySet()) {
            request.setAttribute(modelAttribute.getKey(), modelAttribute.getValue());
        }

        RequestDispatcher rd = request.getRequestDispatcher(this.viewName);
        rd.forward(request, response);
    }
}
