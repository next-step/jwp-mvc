package core.mvc;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class JspView implements View {
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";
    private String viewPath;

    public JspView(String viewPath) {
        this.viewPath = viewPath;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (viewPath.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            response.sendRedirect(viewPath.substring(DEFAULT_REDIRECT_PREFIX.length()));
            return;
        }

        RequestDispatcher rd = request.getRequestDispatcher(viewPath);
        rd.forward(request, response);
    }
}
