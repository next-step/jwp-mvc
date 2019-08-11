package core.mvc.tobe;

import core.mvc.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class JspView implements View {

    private String viewName;

    public JspView(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        for (Map.Entry<String, ?> entry : model.entrySet()) {
            request.setAttribute(entry.getKey(), entry.getValue());
        }

        if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
        }

        request.getRequestDispatcher(viewName).forward(request, response);
    }
}
